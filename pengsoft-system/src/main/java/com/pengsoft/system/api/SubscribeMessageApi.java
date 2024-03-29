package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.SubscribeMessage;
import com.pengsoft.system.service.SubscribeMessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/subscribe-message")
public class SubscribeMessageApi extends EntityApi<SubscribeMessageService, SubscribeMessage, String> {
}
