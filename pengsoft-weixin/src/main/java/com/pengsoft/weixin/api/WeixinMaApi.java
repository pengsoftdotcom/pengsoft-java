package com.pengsoft.weixin.api;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.config.properties.OAuth2Properties;
import com.pengsoft.security.oauth2.provider.token.WeixinMpTokenGranter;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.weixin.service.WeixinMaServices;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * weixin miniapp APIs
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Constant.API_PREFIX + "/weixin/ma/{appid}")
public class WeixinMaApi {

    @Inject
    private WeixinMaServices services;

    @Lazy
    @Inject
    private OAuth2Properties properties;

    private String authorization;

    @Inject
    private UserService userService;

    @PostConstruct
    public void initAuthentication() {
        authorization = properties.getClients().stream()
                .filter(c -> StringUtils.equals(c.getId(), WeixinMpTokenGranter.GRANT_TYPE)).findAny().map(c -> {
                    try {
                        final var authentication = new StringBuilder();
                        authentication.append(c.getId());
                        authentication.append(StringUtils.COLON);
                        authentication.append(c.getSecret());
                        return "Basic " + Base64.getEncoder().encodeToString(authentication.toString().getBytes());
                    } catch (Exception e) {
                        log.error("base 64 encode '{}' error", authorization);
                    }
                    return null;
                }).orElseThrow(() -> new InvalidConfigurationException("oauth2 client config error"));
    }

    protected WxMaService getMaService(String appid) {
        return services.get(appid);
    }

    protected WxMaUserService getUserService(String appid) {
        return getMaService(appid).getUserService();
    }

    protected WxMaSubscribeService getSubscribeService(String appid) {
        return getMaService(appid).getSubscribeService();
    }

    @GetMapping("check")
    public String check(@PathVariable String appid, String timestamp, String nonce, String signature, String echostr) {
        if (getMaService(appid).checkSignature(timestamp, nonce, signature)) {
            return echostr;
        } else {
            return null;
        }
    }

    @SneakyThrows
    @GetMapping("is-bound")
    public Map<String, Object> isBound(@PathVariable String appid, @NotBlank String code) {
        final var session = getWeixinUserSession(appid, code);
        final var openid = session.getOpenid();
        final Map<String, Object> result = new HashMap<>();
        result.put("bound", false);
        result.put("openid", openid);
        userService.findOneByMpOpenid(openid).ifPresent(user -> result.put("bound", true));
        return result;
    }

    private WxMaJscode2SessionResult getWeixinUserSession(String appid, String code) throws WxErrorException {
        return getUserService(appid).getSessionInfo(code);
    }

    @GetMapping("info")
    public WxMaUserInfo info(@PathVariable String appid, String sessionKey, String signature, String rawData,
            String encryptedData, String iv) {
        final var weixinUserService = getUserService(appid);
        if (!weixinUserService.checkUserInfo(sessionKey, rawData, signature)) {
            throw new IllegalArgumentException("user check failed");
        }
        return weixinUserService.getUserInfo(sessionKey, encryptedData, iv);
    }

    @GetMapping("phone")
    public WxMaPhoneNumberInfo phone(@PathVariable String appid, String sessionKey, String signature, String rawData,
            String encryptedData, String iv) {
        final var weixinUserService = getUserService(appid);
        if (!weixinUserService.checkUserInfo(sessionKey, rawData, signature)) {
            throw new IllegalArgumentException("user check failed");
        }
        return weixinUserService.getPhoneNoInfo(sessionKey, encryptedData, iv);
    }

}
