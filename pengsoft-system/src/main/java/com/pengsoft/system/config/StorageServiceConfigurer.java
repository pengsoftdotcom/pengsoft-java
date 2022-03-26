package com.pengsoft.system.config;

import com.pengsoft.system.config.properties.StorageServiceProperties;
import com.pengsoft.system.service.StorageService;
import com.pengsoft.system.service.StorageServiceImpl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ StorageServiceProperties.class })
@ConditionalOnProperty(value = { "pengsoft.system.storage.enabled" }, havingValue = "true")
public class StorageServiceConfigurer {
    @Bean
    public StorageService storageService(StorageServiceProperties properties) {
        return (StorageService) new StorageServiceImpl(properties);
    }
}
