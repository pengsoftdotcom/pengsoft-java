package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.service.PushMessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link PushMessage}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/system/push-message")
public class PushMessageApi extends EntityApi<PushMessageService, PushMessage, String> {

}
