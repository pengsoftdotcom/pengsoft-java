package com.pengsoft.ss.messaging;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.pengsoft.security.domain.User;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.messaging.AbstractMessageBuilder;
import com.pengsoft.system.messaging.MessageBuilder;

/**
 * {@link MessageBuilder} for safety check confirmation.
 * 
 */
@Named
public class SafetyCheckHandleMessageBuilder extends AbstractMessageBuilder {

    @Override
    protected String getTemplateCode() {
        return "safety-check-handle";
    }

    @Override
    protected List<User> getReceivers(Object[] args, Object result) {
        final var check = (SafetyCheck) args[0];
        return List.of(check.getProject().getBuManager().getPerson().getUser());
    }

    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        final var check = (SafetyCheck) args[0];
        if (check.getStatus().getCode().equals("risk")) {
            final var messages = super.build(args, result, types);
            messages.entrySet().stream().flatMap(entry -> entry.getValue().stream())
                    .forEach(message -> message.setParams(Map.of("project", check.getProject().getName())));
            return messages;
        } else {
            return Map.of();
        }
    }

}
