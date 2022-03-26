package com.pengsoft.system.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.pengsoft.system.config.properties.ShortMessageServiceProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.SneakyThrows;

/**
 * 发送消息配置
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@ComponentScan("com.pengsoft.*.messaging")
@EnableConfigurationProperties({ ShortMessageServiceProperties.class })
@Configuration
public class MessagingConfigurer {

    @Bean
    @SneakyThrows
    public Client client(ShortMessageServiceProperties properties) {
        return new Client(new Config().setEndpoint(properties.getEndpoint())
                .setAccessKeyId(properties.getAccessKeyId())
                .setAccessKeySecret(properties.getAccessKeySecret()));
    }

}
