package com.pengsoft.security.aspect;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.config.properties.OAuth2Properties;
import com.pengsoft.security.domain.User;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 处理权限变更切面
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Aspect
public class HandleAuthorityChangedAspect {

    @Lazy
    @Inject
    private TokenStore tokenStore;

    @Lazy
    @Inject
    private OAuth2Properties properties;

    @AfterReturning(pointcut = "@annotation(authorityChanged)", returning = "result")
    public void handle(final JoinPoint jp, final AuthorityChanged authorityChanged, final Object result) {
        if (result instanceof UserDetails userDetails) {
            properties.getClients().forEach(client -> {
                final var tokens = tokenStore.findTokensByClientIdAndUserName(client.getId(),
                        userDetails.getUsername());
                tokens.forEach(token -> {
                    final var authentication = tokenStore.readAuthentication(token);
                    final var userAuthentication = new RememberMeAuthenticationToken(userDetails.getUsername(),
                            userDetails, userDetails.getAuthorities());
                    tokenStore.removeAccessToken(token);
                    tokenStore.storeAccessToken(token,
                            new OAuth2Authentication(authentication.getOAuth2Request(), userAuthentication));
                });
            });
        } else if (jp.getArgs().length > 0 && jp.getArgs()[0] instanceof User user) {
            properties.getClients()
                    .forEach(client -> tokenStore.findTokensByClientIdAndUserName(client.getId(), user.getUsername())
                            .forEach(tokenStore::removeAccessToken));
        }

    }

}
