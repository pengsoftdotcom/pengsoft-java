package com.pengsoft.system.messaging;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.domain.SmsMessage;
import com.pengsoft.system.service.SmsMessageService;

import org.springframework.scheduling.annotation.Async;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Short message service sender.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@Named
public class SmsMessageSender implements MessageSender {

    @Inject
    private Client client;

    @Inject
    private SmsMessageService service;

    @Async
    @SneakyThrows
    @Override
    public void send(@NotNull Message message) {
        final var smsMessage = (SmsMessage) message;
        final var template = smsMessage.getTemplate();
        final var receiver = smsMessage.getReceiver();
        var sentAt = smsMessage.getSentAt();
        final var mobile = receiver.getMobile();
        if (StringUtils.isNotBlank(mobile)) {
            // TODO 定时发送
            final var req = new SendSmsRequest()
                    .setSignName(template.getSignName())
                    .setTemplateCode(template.getTemplateCode())
                    .setPhoneNumbers(mobile)
                    .setTemplateParam("{\"code\":\"1234\"}");
            client.sendSms(req);
            smsMessage.setSentAt(sentAt);
            service.save(smsMessage);
        } else {
            log.warn("sms message not sent cause user({})'s mobile is blank", receiver.getUsername());
        }
    }

}
