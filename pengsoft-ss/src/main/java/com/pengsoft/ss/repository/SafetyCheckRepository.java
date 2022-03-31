package com.pengsoft.ss.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.ss.domain.QSafetyCheck;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyCheck} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyCheckRepository extends EntityRepository<QSafetyCheck, SafetyCheck, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QSafetyCheck root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link SafetyCheck}
     * with the given
     * code.
     *
     * @param code {@link SafetyCheck}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<SafetyCheck> findOneByCode(@NotBlank String code);

}
