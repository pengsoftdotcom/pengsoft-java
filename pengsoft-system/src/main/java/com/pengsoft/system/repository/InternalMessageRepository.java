package com.pengsoft.system.repository;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.QInternalMessage;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link InternalMessage} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface InternalMessageRepository
        extends EntityRepository<QInternalMessage, InternalMessage, String>, OwnedRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QInternalMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringPath::contains);
        bindings.bind(root.content).first(StringPath::contains);
        bindings.bind(root.sentAt).first(DateTimePath::loe);
    }

}
