package com.pengsoft.system.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Messaging auto configure properties
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.system.captcha")
public class CaptchaVerificationFilterProperties {

    private List<String> verificationRequiredUris = List.of();

}
