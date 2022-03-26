package com.pengsoft.system.repository;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.QSmsMessage;
import com.pengsoft.system.domain.SmsMessage;
import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsMessageRepository
        extends EntityRepository<QSmsMessage, SmsMessage, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QSmsMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.content).first(StringExpression::contains);
    }

}
