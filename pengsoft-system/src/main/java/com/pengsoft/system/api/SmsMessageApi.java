package com.pengsoft.system.api;

import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.SmsMessage;
import com.pengsoft.system.service.SmsMessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/sms-message")
public class SmsMessageApi extends EntityApi<SmsMessageService, SmsMessage, String> {
}
