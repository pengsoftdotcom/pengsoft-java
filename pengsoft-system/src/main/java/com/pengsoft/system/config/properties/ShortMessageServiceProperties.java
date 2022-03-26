package com.pengsoft.system.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("pengsoft.system.messaging.sms")
public class ShortMessageServiceProperties {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

}
