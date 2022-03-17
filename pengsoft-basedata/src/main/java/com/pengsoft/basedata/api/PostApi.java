package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.service.PostService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Post}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/post")
public class PostApi extends EntityApi<PostService, Post, String> {

}
