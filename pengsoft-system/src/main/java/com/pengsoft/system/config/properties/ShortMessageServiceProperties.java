package com.pengsoft.system.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Aliyun SMS auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.system.messaging.sms")
public class ShortMessageServiceProperties {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

}
