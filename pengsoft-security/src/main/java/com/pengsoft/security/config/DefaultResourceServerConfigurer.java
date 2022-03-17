package com.pengsoft.security.config;

import javax.inject.Inject;

import com.pengsoft.security.config.properties.WebSecurityProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 资源服务器自动化配置
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@EnableResourceServer
@ConditionalOnProperty(value = "pengsoft.security.oauth2.resource-server.enabled", havingValue = "true")
public class DefaultResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Inject
    private TokenStore tokenStore;

    @Inject
    private WebSecurityProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(final RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    // @formatter:off
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(properties.getUrisPermitted().toArray(String[]::new)).permitAll()
                .anyRequest().authenticated();
    }
    // @formatter:on

}
