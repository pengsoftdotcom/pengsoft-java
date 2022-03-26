package com.pengsoft.system.config;

import com.pengsoft.system.config.properties.CaptchaVerificationFilterProperties;
import com.pengsoft.system.filter.CaptchaVerificationFilter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@ComponentScan({ "com.pengsoft.system.filter" })
@EnableConfigurationProperties({ CaptchaVerificationFilterProperties.class })
@Configuration
public class CaptchaVerificationFilterConfigurer {
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> captchaVerificationFilterRegistrationBean(
            CaptchaVerificationFilterProperties properties, CaptchaVerificationFilter filter) {
        final var registrationBean = new FilterRegistrationBean<OncePerRequestFilter>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(-2147483648);
        registrationBean.setUrlPatterns(properties.getVerificationRequiredUris());
        return registrationBean;
    }
}
