package com.pengsoft.system.repository;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.QSmsMessage;
import com.pengsoft.system.domain.SmsMessage;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SmsMessage} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SmsMessageRepository extends EntityRepository<QSmsMessage, SmsMessage, String>, OwnedRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QSmsMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.content).first(StringPath::contains);
    }

}