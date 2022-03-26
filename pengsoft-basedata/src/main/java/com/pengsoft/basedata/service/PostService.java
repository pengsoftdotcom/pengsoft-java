package com.pengsoft.basedata.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Post}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PostService extends EntityService<Post, String> {

    /**
     * Returns an {@link Optional} of a {@link Post} with the given organization id
     * and
     * name.
     *
     * @param organization The {@link Post}'s organization
     * @param name         The {@link Post}'s name
     */
    Optional<Post> findOneByOrganizationAndName(@NotNull Organization organization, @NotBlank String name);

}
