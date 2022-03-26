package com.pengsoft.system.repository;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.EmailMessage;
import com.pengsoft.system.domain.QEmailMessage;
import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailMessageRepository
        extends EntityRepository<QEmailMessage, EmailMessage, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QEmailMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringExpression::contains);
        bindings.bind(root.content).first(StringExpression::contains);
    }

}
