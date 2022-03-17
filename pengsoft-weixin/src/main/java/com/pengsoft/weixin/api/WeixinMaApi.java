package com.pengsoft.weixin.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.rmi.UnexpectedException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.pengsoft.security.config.properties.OAuth2Properties;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.oauth2.provider.token.WeixinMpTokenGranter;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.weixin.service.WeixinMaServices;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/api/weixin/ma/{appid}")
public class WeixinMaApi {

    private static final String CLIENT_SIGN_IN_ERROR = "Full authentication is required to access this resource";

    private static final String SERVICE_NAME = "weixinMaServices";

    private WeixinMaServices services;

    @Lazy
    @Inject
    private OAuth2Properties properties;

    private String authorization;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    public WeixinMaApi(ApplicationContext context) {
        if (context.containsBean(SERVICE_NAME)) {
            this.services = context.getBean(SERVICE_NAME, WeixinMaServices.class);
        } else {
            log.warn("weixin miniapp services is not up");
        }

    }

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

    private WxMaService getMaService(String appid) {
        return services.get(appid);
    }

    private WxMaUserService getUserService(String appid) {
        return getMaService(appid).getUserService();
    }

    private WxMaSubscribeService getSubscribeService(String appid) {
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

    @GetMapping("sign-in")
    public UserDetails signIn(@PathVariable String appid, @NotBlank String code)
            throws WxErrorException, IOException, InterruptedException {
        final var session = getWeixinUserSession(appid, code);
        final var weixinMpOpenId = session.getOpenid();
        final var client = HttpClient.newHttpClient();
        final var req = buildRequest(weixinMpOpenId);
        dealResponse(weixinMpOpenId, client, req);
        return null;
    }

    private void dealResponse(final String weixinMpOpenId, final HttpClient client, final HttpRequest req)
            throws IOException, InterruptedException {
        final var res = client.send(req, BodyHandlers.ofString());
        final var status = res.statusCode();
        final var type = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class);
        final Map<String, Object> data = objectMapper.readValue(res.body(), type);
        final var description = (String) data.get("error_description");
        switch (status) {
            case 200:
                break;
            case 401:
                if (description.equals(CLIENT_SIGN_IN_ERROR)) {
                    throw new InvalidConfigurationException("oauth client id and secret is not match");
                } else {
                    final var user = userService.createFromWeixin(weixinMpOpenId);
                    roleService.findOneByCode(Role.USER).map(List::of)
                            .ifPresent(roles -> userService.grantRoles(user, roles));
                }
                break;
            default:
                throw new UnexpectedException(description);
        }
    }

    private HttpRequest buildRequest(final String weixinMpOpenId) {
        final var uri = URI.create(properties.getAuthorizationServer().getEndpoint() + "/oauth/token");
        final var body = new StringBuilder();
        body.append("grant_type=weixin_mp&");
        body.append("weixin_mp_open_id=" + weixinMpOpenId).toString();
        return HttpRequest.newBuilder().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(BodyPublishers.ofString(body.toString())).build();
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
