package com.pengsoft.ss.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link ConstructionProject} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface ConstructionProjectRepository
        extends OwnedExtRepository, EntityRepository<QConstructionProject, ConstructionProject, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QConstructionProject root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link ConstructionProject} with the given
     * code.
     *
     * @param code {@link ConstructionProject}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<ConstructionProject> findOneByCode(@NotBlank String code);

}
