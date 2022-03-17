package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;

import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.QIdentityCard;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.support.validation.IdentityNumber;
import com.querydsl.core.types.dsl.DatePath;
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
public interface IdentityCardRepository extends EntityRepository<QIdentityCard, IdentityCard, String>, OwnedRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QIdentityCard root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
        bindings.bind(root.number).first(StringPath::contains);
        bindings.bind(root.issue).first(StringPath::contains);
        bindings.bind(root.startDate).first(DatePath::before);
        bindings.bind(root.endDate).first(DatePath::after);
    }

    /**
     * Returns an {@link Optional} of a {@link IdentityCard} with the given number.
     *
     * @param number {@link IdentityCard}'s number
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<IdentityCard> findOneByNumber(@IdentityNumber String number);

}
