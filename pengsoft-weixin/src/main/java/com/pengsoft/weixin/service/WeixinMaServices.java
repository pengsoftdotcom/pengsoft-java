package com.pengsoft.weixin.service;

import java.util.HashMap;
import java.util.Map;

import cn.binarywang.wx.miniapp.api.WxMaService;

public class WeixinMaServices {

    private Map<String, WxMaService> services;

    public WeixinMaServices() {
        this(new HashMap<>());
    }

    public WeixinMaServices(Map<String, WxMaService> services) {
        this.services = services;
    }

    public void put(String appid, WxMaService service) {
        services.put(appid, service);
    }

    public WxMaService get(String appid) {
        return services.get(appid);
    }

}
