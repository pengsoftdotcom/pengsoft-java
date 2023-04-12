package com.pengsoft.system.repository;

import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.QSubscribeMessage;
import com.pengsoft.system.domain.SubscribeMessage;
import com.querydsl.core.types.dsl.StringExpression;

public interface SubscribeMessageRepository extends EntityRepository<QSubscribeMessage, SubscribeMessage, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QSubscribeMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.content).first(StringExpression::contains);
    }

}
