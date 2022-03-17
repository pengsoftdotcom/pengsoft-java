package com.pengsoft.basedata.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Optical character recognition service auto configure properties.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.recognition")
public class RecognitionServiceProperties {

    private boolean enabled;

    private String endpoint;

}
