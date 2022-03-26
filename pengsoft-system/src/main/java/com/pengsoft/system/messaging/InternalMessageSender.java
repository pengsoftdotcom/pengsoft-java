package com.pengsoft.system.messaging;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.service.InternalMessageService;

import org.apache.commons.text.StringSubstitutor;

@Named
public class InternalMessageSender implements MessageSender {

    @Inject
    private InternalMessageService service;

    public void send(@NotNull Message message) {
        final var internalMessage = (InternalMessage) message;
        final var substitutor = new StringSubstitutor(message.getParams());
        internalMessage.setSubject(substitutor.replace(internalMessage.getSubject()));
        internalMessage.setContent(substitutor.replace(internalMessage.getContent()));
        internalMessage.setSentAt(DateUtils.currentDateTime());
        service.save(internalMessage);
    }

}
