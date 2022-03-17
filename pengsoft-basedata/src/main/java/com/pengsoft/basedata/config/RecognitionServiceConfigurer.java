package com.pengsoft.basedata.config;

import com.pengsoft.basedata.config.properties.RecognitionServiceProperties;
import com.pengsoft.basedata.service.RecognitionService;
import com.pengsoft.basedata.service.RecognitionServiceImpl;
import com.pengsoft.system.config.properties.StorageServiceProperties;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.system.service.RegionService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Optical character recognition service auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ StorageServiceProperties.class, RecognitionServiceProperties.class })
@ConditionalOnProperty(value = "pengsoft.recognition.enabled", havingValue = "true")
public class RecognitionServiceConfigurer {

    @Bean
    public RecognitionService recognitionService(ApplicationContext context,
            DictionaryItemService dictionaryItemService, RegionService regionService) {
        StorageServiceProperties storageServiceProperties = null;
        if (context.containsBean("storageServiceProperties")) {
            storageServiceProperties = context.getBean(StorageServiceProperties.class);
        }
        RecognitionServiceProperties recognitionServiceProperties = null;
        if (context.containsBean("recognitionServiceProperties")) {
            recognitionServiceProperties = context.getBean(RecognitionServiceProperties.class);
        }
        return new RecognitionServiceImpl(storageServiceProperties, recognitionServiceProperties,
                dictionaryItemService.findAllByTypeCode("gender"),
                dictionaryItemService.findAllByTypeCode("nationality"), regionService);
    }

}
