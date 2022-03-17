package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.EmailMessage;
import com.pengsoft.system.service.EmailMessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link EmailMessage}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/system/email-message")
public class EmailMessageApi extends EntityApi<EmailMessageService, EmailMessage, String> {

}
