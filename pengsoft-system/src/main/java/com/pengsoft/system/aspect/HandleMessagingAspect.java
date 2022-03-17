package com.pengsoft.system.aspect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.messaging.MessageBuilder;
import com.pengsoft.system.messaging.MessageSender;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * 发送消息切面
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Aspect
public class HandleMessagingAspect {

    @Inject
    private Map<String, MessageBuilder> builders;

    @Inject
    private Map<String, MessageSender> senders;

    @AfterReturning(pointcut = "@annotation(messaging)", returning = "result")
    public void handle(final JoinPoint jp, final Messaging messaging, final Object result) {
        final var builder = builders.get(messaging.builder());
        if (builder == null) {
            throw new InvalidConfigurationException("No such MessageBuilder:" + messaging.builder());
        }
        final var messages = builder.build(jp.getArgs(), result, messaging.types());
        for (Entry<String, List<Message>> entry : messages.entrySet()) {
            final var sender = senders.get(entry.getKey() + "MessageSender");
            entry.getValue().forEach(sender::send);
        }
    }

}
