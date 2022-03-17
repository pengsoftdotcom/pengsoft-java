package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.QPost;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Post} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PostRepository extends EntityRepository<QPost, Post, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QPost root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Post} with the given organization id
     * and
     * name.
     *
     * @param organizationId The id of {@link Post}'s organization
     * @param name           The {@link Post}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Post> findOneByOrganizationIdAndName(@NotBlank String organizationId, String name);

}
