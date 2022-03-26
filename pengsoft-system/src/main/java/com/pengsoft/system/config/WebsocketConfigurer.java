package com.pengsoft.system.config;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@ComponentScan("com.pengsoft.*.ws")
public class WebsocketConfigurer extends WebSocketConfigurationSupport {

    @Inject
    private List<WebSocketHandler> handlers;

    @Override
    protected void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        handlers.forEach(handler -> registry.addHandler(handler, getPath(handler)));
    }

    private String getPath(WebSocketHandler handler) {
        return "/ws/" + handler.getClass().getSimpleName().replace("WebSocketHandler", "");
    }

}
