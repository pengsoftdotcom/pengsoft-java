package com.pengsoft.system.messaging;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.domain.SmsMessage;
import com.pengsoft.system.domain.SmsMessageTemplate;
import com.pengsoft.system.service.SmsMessageService;
import com.pengsoft.system.service.SystemParamService;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import lombok.SneakyThrows;

@Named
public class SmsMessageSender
        implements MessageSender {
    private static final Logger log = LoggerFactory.getLogger(SmsMessageSender.class);

    @Inject
    private Client client;

    @Inject
    private SmsMessageService service;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private SystemParamService systemParamService;

    @SneakyThrows
    @Async
    public void send(@NotNull Message message) {
        SmsMessage smsMessage = (SmsMessage) message;
        SmsMessageTemplate template = (SmsMessageTemplate) smsMessage.getTemplate();
        User receiver = smsMessage.getReceiver();
        LocalDateTime sentAt = smsMessage.getSentAt();
        String mobile = receiver.getMobile();
        if (StringUtils.isNotBlank(mobile)) {
            systemParamService.findOneByCode("sms_switch").ifPresent(systemParam -> {
                if ((boolean) systemParam.getParam().get("value")) {
                    try {
                        SendSmsRequest req = (new SendSmsRequest()).setSignName(template.getSignName())
                                .setTemplateCode(template.getTemplateCode()).setPhoneNumbers(mobile)
                                .setTemplateParam(this.objectMapper.writeValueAsString(smsMessage.getParams()));
                        this.client.sendSms(req);
                    } catch (Exception e) {
                        log.warn("sms message not sent cause' {}", e.getMessage());
                    }
                } else {
                    log.info("sms message not sent cause' sms_switch set to false");
                }
            });
            smsMessage.setSentAt(sentAt);
            smsMessage.setContent(new StringSubstitutor(smsMessage.getParams()).replace(smsMessage.getContent()));
            this.service.save(smsMessage);
        } else {
            log.warn("sms message not sent cause user({})'s mobile is blank", receiver.getUsername());
        }
    }
}
