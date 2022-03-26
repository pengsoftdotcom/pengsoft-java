package com.pengsoft.system.repository;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.domain.QPushMessage;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link PushMessage} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PushMessageRepository extends EntityRepository<QPushMessage, PushMessage, String>, OwnedRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QPushMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringPath::contains);
        bindings.bind(root.content).first(StringPath::contains);
    }

}