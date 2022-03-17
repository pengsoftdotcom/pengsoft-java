package com.pengsoft.weixin.config;

import java.util.stream.Collectors;

import com.pengsoft.weixin.config.properties.WeixinMaProperties;
import com.pengsoft.weixin.service.WeixinMaServices;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;

/**
 * Weixin miniapp configuration
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(WeixinMaProperties.class)
public class WeixinMaConfigurer {

    @Bean
    public WeixinMaServices weixinMaServices(WeixinMaProperties properties) {
        return new WeixinMaServices(properties.getConfigs().stream().map(a -> {
            final var config = new WxMaDefaultConfigImpl();
            config.setAppid(a.getAppid());
            config.setSecret(a.getSecret());
            config.setToken(a.getToken());
            config.setAesKey(a.getAesKey());
            config.setMsgDataFormat(a.getMsgDataFormat());

            final var service = new WxMaServiceImpl();
            service.setWxMaConfig(config);
            return service;
        }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a)));
    }

}
