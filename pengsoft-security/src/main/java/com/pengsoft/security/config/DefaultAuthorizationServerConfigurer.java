package com.pengsoft.security.config;

import java.util.ArrayList;

import javax.inject.Inject;

import com.pengsoft.security.config.properties.OAuth2Properties;
import com.pengsoft.security.config.properties.WebSecurityProperties;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.json.OAuth2AccessTokenMixIn;
import com.pengsoft.security.oauth2.provider.token.WeixinMpTokenGranter;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.json.ObjectMapper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ WebSecurityProperties.class, OAuth2Properties.class })
@EnableAuthorizationServer
@ConditionalOnProperty(value = "pengsoft.security.oauth2.authorization-server.enabled", havingValue = "true")
public class DefaultAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private WebSecurityProperties webSecurityProperties;

    @Inject
    private OAuth2Properties oauth2Properties;

    @Inject
    private TokenStore tokenStore;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private UserService userService;

    public DefaultAuthorizationServerConfigurer(final ObjectMapper objectMapper) {
        objectMapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixIn.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(final RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        var builder = clients.inMemory();
        oauth2Properties.getClients()
                .forEach(client -> builder.withClient(client.getId()).secret(passwordEncoder.encode(client.getSecret()))
                        .authorizedGrantTypes(client.getGrantTypes().toArray(String[]::new)).scopes(client.getScope())
                        .accessTokenValiditySeconds(client.getAccessTokenValiditySeconds()));
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.getClientDetailsService();
        endpoints.authenticationManager(authenticationManager).allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .tokenStore(tokenStore).userDetailsService(userDetailsService).tokenGranter(getTokenGranter(endpoints));
    }

    private TokenGranter getTokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        final var tokenServices = endpoints.getTokenServices();
        final var authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        final var clientDetailsService = endpoints.getClientDetailsService();
        final var requestFactory = endpoints.getOAuth2RequestFactory();
        final var tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices,
                clientDetailsService, requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory));
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));
        tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                clientDetailsService, requestFactory));
        tokenGranters
                .add(new WeixinMpTokenGranter(userDetailsService, tokenServices, clientDetailsService, requestFactory));

        return new CompositeTokenGranter(tokenGranters);
    }

    @EventListener
    public void authenticationSuccessEventListener(final AuthenticationSuccessEvent event) {
        final var authentication = event.getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken
                && authentication.getPrincipal() instanceof DefaultUserDetails) {
            userService.signInSuccess(authentication.getName());
        }
    }

    @EventListener
    public void authenticationFailureEventListener(final AbstractAuthenticationFailureEvent event) {
        final var exception = event.getException();
        final var authentication = event.getAuthentication();
        if (exception instanceof BadCredentialsException) {
            userService.signInFailure(authentication.getName(), webSecurityProperties.getAllowSignInFailure());
        }
    }

}
