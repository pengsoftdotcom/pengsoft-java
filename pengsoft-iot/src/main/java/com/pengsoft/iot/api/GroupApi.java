package com.pengsoft.iot.api;

import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.service.GroupService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.TreeEntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Group}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/iot/group")
public class GroupApi extends TreeEntityApi<GroupService, Group, String> {

}
