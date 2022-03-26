package com.pengsoft.system.repository;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.QInternalMessage;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalMessageRepository
        extends EntityRepository<QInternalMessage, InternalMessage, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QInternalMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringExpression::contains);
        bindings.bind(root.content).first(StringExpression::contains);
        bindings.bind(root.sentAt).first(ComparableExpression::loe);
    }

}
