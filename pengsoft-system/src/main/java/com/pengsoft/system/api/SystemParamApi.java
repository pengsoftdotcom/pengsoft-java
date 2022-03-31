package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.SystemParam;
import com.pengsoft.system.service.SystemParamService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/system-param")
public class SystemParamApi extends EntityApi<SystemParamService, SystemParam, String> {

}
