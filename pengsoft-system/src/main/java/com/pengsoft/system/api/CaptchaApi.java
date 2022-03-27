package com.pengsoft.system.api;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.User;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.validation.Mobile;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.json.CaptchaWrapper;
import com.pengsoft.system.service.CaptchaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/captcha")
public class CaptchaApi extends EntityApi<CaptchaService, Captcha, String> {
    @Inject
    private UserService userService;

    @PostMapping({ "generate" })
    public CaptchaWrapper generate(@NotBlank @Mobile String username) {
        User user = (User) this.userService.findOneByMobile(username)
                .orElseThrow(() -> getExceptions().constraintViolated("username", "not_exists", new Object[0]));
        return new CaptchaWrapper(getService().generate(user));
    }
}
