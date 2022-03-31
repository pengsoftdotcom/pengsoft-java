package com.pengsoft.system.messaging;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.pengsoft.security.domain.User;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.json.CaptchaWrapper;

@Named
public class CaptchaMessageBuilder extends AbstractMessageBuilder {

    protected String getTemplateCode() {
        return "captcha";
    }

    protected List<User> getReceivers(Object[] args, Object result) {
        Captcha captcha = ((CaptchaWrapper) result).getCaptcha();
        return List.of(captcha.getUser());
    }

    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        Map<String, List<Message>> messages = super.build(args, result, types);
        Captcha captcha = ((CaptchaWrapper) result).getCaptcha();
        messages.entrySet().stream().flatMap(entry -> entry.getValue().stream())
                .forEach(message -> message.setParams(Map.of("code", captcha.getCode())));
        return messages;
    }

}
