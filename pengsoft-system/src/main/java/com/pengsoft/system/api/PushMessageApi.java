package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.service.PushMessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/push-message")
public class PushMessageApi extends EntityApi<PushMessageService, PushMessage, String> {
}
