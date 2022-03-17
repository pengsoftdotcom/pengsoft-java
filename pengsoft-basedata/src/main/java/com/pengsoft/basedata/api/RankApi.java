package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Rank;
import com.pengsoft.basedata.service.RankService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Rank}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/rank")
public class RankApi extends EntityApi<RankService, Rank, String> {

}
