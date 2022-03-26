package com.pengsoft.system.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("pengsoft.system.captcha")
public class CaptchaVerificationFilterProperties {

    private List<String> verificationRequiredUris = List.of();

}
