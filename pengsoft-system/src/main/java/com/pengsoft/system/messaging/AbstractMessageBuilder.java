package com.pengsoft.system.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.pengsoft.security.domain.User;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.CompositeMessageTemplate;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.domain.MessageTemplate;
import com.pengsoft.system.service.CompositeMessageTemplateService;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.Getter;
import lombok.SneakyThrows;

public abstract class AbstractMessageBuilder implements MessageBuilder {

    @Inject
    private CompositeMessageTemplateService compositeMessageTemplateService;

    @Getter
    @Inject
    private UserService userService;

    @Getter
    @Inject
    private Exceptions exceptions;

    protected CompositeMessageTemplate getCompositeMessageTemplate(String templateCode) {
        return (CompositeMessageTemplate) this.compositeMessageTemplateService.findOneByCode(templateCode)
                .orElseThrow(() -> this.exceptions.entityNotExists(templateCode));
    }

    protected User getSender(Object[] args, Object result) {
        return (User) this.userService.findOneByUsername("admin")
                .orElseThrow(() -> this.exceptions.entityNotExists("admin"));
    }

    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        HashMap<String, List<Message>> messages = new HashMap<>();
        CompositeMessageTemplate compositeMessageTemplate = getCompositeMessageTemplate(getTemplateCode());
        User sender = getSender(args, result);
        for (String type : types) {
            MessageTemplate messageTemplate = getMessageTemplate(compositeMessageTemplate, type);
            if (messageTemplate != null) {
                List<User> receivers = getReceivers(args, result);
                messages.put(type, receivers.stream().map(receiver -> {
                    Message message = messageTemplate.toMessage(sender, receiver);
                    message.setTemplate(messageTemplate);
                    return message;
                }).toList());
            }
        }
        return messages;
    }

    @SneakyThrows
    private MessageTemplate getMessageTemplate(CompositeMessageTemplate compositeMessageTemplate, String type) {
        String methodName = "get" + StringUtils.capitalize(type);
        return (MessageTemplate) MethodUtils.invokeMethod(compositeMessageTemplate, methodName);
    }

    protected abstract String getTemplateCode();

    protected abstract List<User> getReceivers(Object[] paramArrayOfObject, Object paramObject);
}
