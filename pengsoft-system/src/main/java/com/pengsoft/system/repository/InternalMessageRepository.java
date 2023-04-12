package com.pengsoft.system.repository;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.QInternalMessage;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.StringExpression;

public interface InternalMessageRepository extends EntityRepository<QInternalMessage, InternalMessage, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QInternalMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringExpression::contains);
        bindings.bind(root.content).first(StringExpression::contains);
        bindings.bind(root.sentAt).first(ComparableExpression::loe);
    }

    /**
     * 返回收件人未读消息数
     * 
     * @param receiverId 收件人ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByReceiverIdAndReadAtIsNull(@NotBlank String receiverId);

}
