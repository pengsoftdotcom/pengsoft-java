package com.pengsoft.basedata.service;

import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.repository.PostRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PostService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PostServiceImpl extends EntityServiceImpl<PostRepository, Post, String> implements PostService {

    @Override
    public Post save(final Post post) {
        getRepository().findOneByOrganizationIdAndName(post.getOrganization().getId(), post.getName())
                .ifPresent(source -> {
                    if (EntityUtils.notEquals(source, post)) {
                        throw getExceptions().constraintViolated("name", "exists", post.getName());
                    }
                });
        return super.save(post);
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Direction.DESC, "rank.sequence");
    }

}
