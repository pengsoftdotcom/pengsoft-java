package com.pengsoft.system.config;

import javax.inject.Inject;

import com.pengsoft.system.ws.InternalMessageWebSocketHandler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@ComponentScan("com.pengsoft.*.ws")
public class WebsocketConfigurer implements WebSocketConfigurer {

    @Inject
    private InternalMessageWebSocketHandler internalMessageWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(internalMessageWebSocketHandler, "/internal-message").setAllowedOrigins("*");
    }

}
