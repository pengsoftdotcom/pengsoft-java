package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.AuthenticationRecord;
import com.pengsoft.basedata.service.AuthenticationRecordService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link AuthenticationRecord}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/authentication-record")
public class AuthenticationRecordApi extends EntityApi<AuthenticationRecordService, AuthenticationRecord, String> {

}
