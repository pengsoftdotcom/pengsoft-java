package com.pengsoft.system.ws;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.service.UserService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.system.service.InternalMessageService;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
public class InternalMessageWebSocketHandler extends TextWebSocketHandler {

    @Inject
    private WebSocketSessions sessions;

    @Inject
    private InternalMessageService internalMessageService;

    @Inject
    private UserService userService;

    @Inject
    private Exceptions exceptions;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final var principal = session.getPrincipal();
        if (principal != null) {
            final var username = principal.getName();
            log.debug(username + "连接");
            sessions.put(username, session);
            final var receiver = userService.findOneByUsername(username)
                    .orElseThrow(() -> exceptions.entityNotExists(username));
            final var unreadCount = internalMessageService.countByReceiverAndReadAtIsNull(receiver);
            session.sendMessage(new TextMessage(String.valueOf(unreadCount)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final var principal = session.getPrincipal();
        if (principal != null) {
            log.debug(principal.getName() + "断开");
            sessions.put(principal.getName(), session);
        }
    }

}
