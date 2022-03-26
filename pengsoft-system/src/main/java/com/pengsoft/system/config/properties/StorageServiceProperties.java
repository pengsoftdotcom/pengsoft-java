package com.pengsoft.system.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("pengsoft.system.storage")
public class StorageServiceProperties {

    private boolean enabled;

    private String accessKeyId;

    private String accessKeySecret;

    private String publicBucket;

    private String lockedBucket;

    private String publicBucketEndpoint;

    private String lockedBucketEndpoint;

    private String publicAccessPathPrefix;

    private String lockedAccessPathPrefix;

}
