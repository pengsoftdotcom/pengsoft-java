package com.pengsoft.system.repository;

import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.domain.QPushMessage;
import com.querydsl.core.types.dsl.StringExpression;

public interface PushMessageRepository extends EntityRepository<QPushMessage, PushMessage, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QPushMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringExpression::contains);
        bindings.bind(root.content).first(StringExpression::contains);
    }

}
