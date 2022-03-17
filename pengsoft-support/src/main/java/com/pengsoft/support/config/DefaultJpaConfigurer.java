package com.pengsoft.support.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.support.Repositories;

/**
 * JPA auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@ComponentScan({ "com.pengsoft.*.facade", "com.pengsoft.*.service" })
@EnableJpaRepositories({ "com.pengsoft.*.repository" })
@EntityScan({ "com.pengsoft.*.domain" })
public class DefaultJpaConfigurer {

    @Bean
    public Repositories repositories(ApplicationContext context) {
        return new Repositories(context);
    }

}
