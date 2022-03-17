package com.pengsoft.security.aspect;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Named;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.Owned;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.security.access.AccessDeniedException;

/**
 * {@link ApiMethodArgsDataAuthorityHandler} for {@link Owned}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class OwnedEntityApiMethodArgsDataAuthorityHandler implements ApiMethodArgsDataAuthorityHandler {

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
        if (isReadable(authorized, entityClass) || hasAdminRole(entityClass)) {
            return predicate;
        } else {
            final var createdByPath = (StringPath) QueryDslUtils.getPath(entityClass, "createdBy");
            return createdByPath.eq(SecurityUtils.getUserId()).and(predicate);
        }
    }

    protected boolean isAuthorized(final OwnedEntityImpl entity, Authorized authorized) {
        final var entityClass = entity.getClass();
        if (StringUtils.isBlank(entity.getId()) || isWritable(authorized, entityClass) || hasAdminRole(entityClass)) {
            return true;
        } else {
            return StringUtils.equals(SecurityUtils.getUserId(), entity.getCreatedBy());
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
