package com.pengsoft.support.repository;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.QueryHint;

import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.validation.annotation.Validated;

/**
 * The repository interface of {@link EntityImpl} based on JPA
 *
 * @author peng.dang@pengsoft.com
 */
@Validated
@NoRepositoryBean
public interface EntityRepository<Q extends EntityPathBase<T>, T extends Entity<ID>, ID extends Serializable>
        extends JpaRepositoryImplementation<T, ID>, QuerydslPredicateExecutor<T>, QuerydslBinderCustomizer<Q> {

    @Override
    default void customize(final QuerydslBindings bindings, final Q root) {
        final var entityClass = getEntityClass(root);
        final var idClass = getIdClass(root);
        if (idClass.equals(String.class)) {
            final StringPath idPath = QueryDslUtils.getIdStringPath(entityClass);
            bindings.bind(idPath).all((path, value) -> Optional.of(path.in(value)));
        } else if (Number.class.isAssignableFrom(idClass)) {
            final NumberPath<?> idPath = QueryDslUtils.getIdNumberPath(entityClass);
            bindings.bind(idPath).all((path, value) -> Optional.of(path.in(value)));
        }
    }

    default Class<? extends T> getEntityClass(final Q root) {
        return root.getType();
    }

    @SuppressWarnings("unchecked")
    default Class<ID> getIdClass(final Q root) {
        return (Class<ID>) MethodUtils.getMatchingMethod(root.getType(), "getId").getReturnType();
    }

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<T> findById(ID id);

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<T> findOne(Predicate predicate);

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Page<T> findAll(Predicate predicate, Pageable pageable);

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Iterable<T> findAll(Predicate predicate, Sort sort);

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    boolean exists(Predicate predicate);

    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long count(Predicate predicate);

}
