package com.pengsoft.system.config;

import com.pengsoft.system.config.properties.CaptchaVerificationFilterProperties;
import com.pengsoft.system.filter.CaptchaVerificationFilter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 验证验证码过滤器配置
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@ComponentScan({ "com.pengsoft.system.filter" })
@EnableConfigurationProperties(CaptchaVerificationFilterProperties.class)
@Configuration
public class CaptchaVerificationFilterConfigurer {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> captchaVerificationFilterRegistrationBean(
            final CaptchaVerificationFilterProperties properties, CaptchaVerificationFilter filter) {
        final FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setUrlPatterns(properties.getVerificationRequiredUris());
        return registrationBean;
    }

}
