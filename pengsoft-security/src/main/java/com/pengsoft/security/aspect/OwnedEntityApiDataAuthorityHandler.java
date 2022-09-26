package com.pengsoft.security.aspect;

import java.lang.reflect.Method;

import javax.inject.Named;

import com.pengsoft.security.domain.Owned;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * {@link ApiDataAuthorityHandler} for {@link Owned}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Named
public class OwnedEntityApiDataAuthorityHandler<T extends OwnedEntityImpl> extends AbstractApiDataAuthorityHandler<T> {

    @Override
    public boolean support(Class<?> entityClass) {
        return OwnedEntityImpl.class.isAssignableFrom(entityClass);
    }

    @Override
    protected Predicate exchange(Class<T> entityClass, Method method, Predicate predicate) {
        final var createdByPath = (StringPath) QueryDslUtils.getPath(entityClass, "createdBy");
        return createdByPath.eq(SecurityUtils.getUserId()).and(predicate);
    }

    @Override
    protected boolean isAuthorized(Method method, T entity) {
        if (StringUtils.isBlank(entity.getId())) {
            return true;
        } else {
            return StringUtils.equals(SecurityUtils.getUserId(), entity.getCreatedBy());
        }
    }

}
