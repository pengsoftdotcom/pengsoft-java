package com.pengsoft.system.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.CompositeMessageTemplate;
import com.pengsoft.system.domain.QCompositeMessageTemplate;
import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CompositeMessageTemplateRepository
        extends EntityRepository<QCompositeMessageTemplate, CompositeMessageTemplate, String> {

    @Override
    default void customize(QuerydslBindings bindings, QCompositeMessageTemplate root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringExpression::contains);
        bindings.bind(root.name).first(StringExpression::contains);
    }

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    Optional<CompositeMessageTemplate> findOneByCode(@NotBlank String code);
}
