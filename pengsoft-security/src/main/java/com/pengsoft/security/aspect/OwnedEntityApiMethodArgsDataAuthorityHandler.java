package com.pengsoft.security.aspect;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.Owned;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.access.AccessDeniedException;

import lombok.SneakyThrows;

/**
 * {@link ApiMethodArgsDataAuthorityHandler} for {@link Owned}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class OwnedEntityApiMethodArgsDataAuthorityHandler implements ApiMethodArgsDataAuthorityHandler {

    @Inject
    private Repositories repositories;

    @Inject
    private Exceptions exceptions;

    @Override
    public boolean support(Class<?> entityClass) {
        return Owned.class.isAssignableFrom(entityClass);
    }

    @Override
    public Object[] handle(Class<? extends OwnedEntityImpl> entityClass, Authorized authorized, Object[] args)
            throws AccessDeniedException {
        return Arrays.stream(args).map(arg -> {
            if (arg instanceof Predicate predicate) {
                return exchange(entityClass, authorized, predicate);
            } else if (arg instanceof OwnedEntityImpl entity && !isAuthorized(entity, authorized)) {
                throw new AccessDeniedException("Not authorized");
            } else if (arg instanceof Collection<?> entities
                    && entities.stream().allMatch(OwnedEntityImpl.class::isInstance)
                    && !entities.stream().map(OwnedEntityImpl.class::cast)
                            .allMatch(entity -> isAuthorized(entity, authorized))) {
                throw new AccessDeniedException("Not authorized");
            } else {
                return arg;
            }
        }).toArray();
    }

    protected Predicate exchange(Class<? extends OwnedEntityImpl> entityClass, Authorized authorized,
            Predicate predicate) {
        if (isReadable(authorized, entityClass) || hasAdminRole(entityClass)
                || predicate.toString().contains("createdBy")) {
            return predicate;
        } else {
            final var createdByPath = (StringPath) QueryDslUtils.getPath(entityClass, "createdBy");
            return createdByPath.eq(SecurityUtils.getUserId()).and(predicate);
        }
    }

    protected boolean isAuthorized(OwnedEntityImpl entity, Authorized authorized) {
        entity = getEntity(entity);
        final var entityClass = entity.getClass();
        if (StringUtils.isBlank(entity.getId()) || isWritable(authorized, entityClass) || hasAdminRole(entityClass)) {
            return true;
        } else {
            return StringUtils.equals(SecurityUtils.getUserId(), entity.getCreatedBy());
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected OwnedEntityImpl getEntity(final OwnedEntityImpl entity) {
        if (StringUtils.isNotBlank(entity.getId()) && StringUtils.isBlank(entity.getCreatedBy())) {
            final var entityClass = entity.getClass();
            final var repository = repositories.getRepositoryFor(entityClass).map(CrudRepository.class::cast)
                    .orElseThrow(() -> new InvalidConfigurationException("No repository for " + entityClass.getName()));
            return (OwnedEntityImpl) repository.findById(entity.getId())
                    .orElseThrow(() -> exceptions.entityNotExists(entityClass, entity.getId()));
        } else {
            return entity;
        }
    }

    protected boolean hasAdminRole(final Class<? extends Entity<? extends Serializable>> entityClass) {
        return SecurityUtils.hasAnyRole(Role.ADMIN,
                SecurityUtils.getModuleAdminRoleCode(entityClass),
                SecurityUtils.getEntityAdminRoleCode(entityClass));
    }

    protected boolean isReadable(Authorized authorized, Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getAnnotation(Authorized.class)).orElse(authorized).readable();
    }

    protected boolean isWritable(Authorized authorized, Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getAnnotation(Authorized.class)).orElse(authorized).writable();
    }

}
