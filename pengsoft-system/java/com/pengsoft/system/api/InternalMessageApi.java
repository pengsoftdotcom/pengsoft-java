package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.service.InternalMessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link InternalMessage}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/system/internal-message")
public class InternalMessageApi extends EntityApi<InternalMessageService, InternalMessage, String> {

}
