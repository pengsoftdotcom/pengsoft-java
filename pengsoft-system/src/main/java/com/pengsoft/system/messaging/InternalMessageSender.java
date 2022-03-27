package com.pengsoft.system.messaging;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.service.InternalMessageService;
import com.pengsoft.system.ws.WebSocketSessions;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.web.socket.TextMessage;

import lombok.SneakyThrows;

@Named
public class InternalMessageSender implements MessageSender {

    @Inject
    private InternalMessageService service;

    @Inject
    private WebSocketSessions sessions;

    @SneakyThrows
    public void send(@NotNull Message message) {
        final var internalMessage = (InternalMessage) message;
        final var substitutor = new StringSubstitutor(message.getParams());
        internalMessage.setSubject(substitutor.replace(internalMessage.getSubject()));
        internalMessage.setContent(substitutor.replace(internalMessage.getContent()));
        internalMessage.setSentAt(DateUtils.currentDateTime());
        service.save(internalMessage);

        final var receiver = message.getReceiver();
        final var unreadCount = service.countByReceiverAndReadAtIsNull(receiver);
        final var session = sessions.get(receiver.getUsername());
        if (session != null) {
            session.sendMessage(new TextMessage(String.valueOf(unreadCount)));
        }
    }

}
