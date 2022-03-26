package com.pengsoft.ss.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyTraining} based
 * on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyTrainingRepository
        extends OwnedExtRepository, EntityRepository<QSafetyTraining, SafetyTraining, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QSafetyTraining root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.subject).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link SafetyTraining}
     * with the given
     * code.
     *
     * @param code {@link SafetyTraining}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<SafetyTraining> findOneByCode(@NotBlank String code);

}
