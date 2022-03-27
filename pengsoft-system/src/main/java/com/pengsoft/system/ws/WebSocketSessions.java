package com.pengsoft.system.ws;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.springframework.web.socket.WebSocketSession;

@Named
public class WebSocketSessions {

    private Map<String, WebSocketSession> sessions = new HashMap<>();

    public void put(String id, WebSocketSession session) {
        sessions.put(id, session);
    }

    public WebSocketSession get(String id) {
        return sessions.get(id);
    }

    public void remove(String id) {
        sessions.remove(id);
    }

}