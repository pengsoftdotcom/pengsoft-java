package com.pengsoft.system.aspect;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.messaging.MessageBuilder;
import com.pengsoft.system.messaging.MessageSender;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
@Aspect
public class HandleMessagingAspect {

    @Inject
    private Map<String, MessageBuilder> builders;

    @Inject
    private Map<String, MessageSender> senders;

    @AfterReturning(pointcut = "@annotation(messaging)", returning = "result")
    public void handle(JoinPoint jp, Messaging messaging, Object result) {
        try {
            final var builder = this.builders.get(messaging.builder());
            if (builder == null) {
                throw new InvalidConfigurationException("No such MessageBuilder:" + messaging.builder());
            }
            final var messages = builder.build(jp.getArgs(), result, messaging.types());
            for (final var entry : messages.entrySet()) {
                final var name = entry.getKey() + "MessageSender";
                final var sender = this.senders.get(name);
                if (sender != null) {
                    entry.getValue().forEach(sender::send);
                }
                log.warn("message not send cause sender({}) not found", name);
            }

        } catch (Exception e) {
            log.error("send message error: {}", e.getMessage());
        }
    }
}
