package com.pengsoft.system.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Captcha;

public interface CaptchaService extends EntityService<Captcha, String> {

    Captcha generate(@NotNull User user);

    boolean isValid(@NotNull User user, @NotBlank String code);

}
