package com.pengsoft.weixin.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Weixin miniapp configuration properties
 * 
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "pengsoft.weixin.miniapp")
public class WeixinMaProperties {

    private boolean enabled;

    private List<Config> configs;

    @Getter
    @Setter
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;
    }

}
