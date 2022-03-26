package com.pengsoft.system.json;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.system.domain.Captcha;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = CaptchaWrapperJsonSerializer.class)
public class CaptchaWrapper implements Serializable {

    private static final long serialVersionUID = 5958167478830762828L;

    private Captcha captcha;
    
    public CaptchaWrapper(Captcha captcha) {
    	setCaptcha(captcha);
    }

}
