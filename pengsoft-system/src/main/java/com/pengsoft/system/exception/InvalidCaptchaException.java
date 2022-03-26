package com.pengsoft.system.exception;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

public class InvalidCaptchaException extends ClientAuthenticationException {

    private static final long serialVersionUID = -3854602592228855797L;

    public InvalidCaptchaException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvalidCaptchaException(String message) {
        super(message);
    }

    public String getOAuth2ErrorCode() {
        return "invalid_captcha";
    }

}
