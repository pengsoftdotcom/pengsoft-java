package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.service.CodingRuleService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link CodingRule}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/coding-rule")
public class CodingRuleApi extends EntityApi<CodingRuleService, CodingRule, String> {

}
