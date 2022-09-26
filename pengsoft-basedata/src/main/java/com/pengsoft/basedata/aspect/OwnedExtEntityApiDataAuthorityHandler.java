package com.pengsoft.basedata.aspect;

import java.lang.reflect.Method;

import javax.inject.Named;

import com.pengsoft.basedata.domain.OwnedExt;
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.aspect.ApiDataAuthorityHandler;
import com.pengsoft.security.aspect.OwnedEntityApiDataAuthorityHandler;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * The implementer of {@link ApiDataAuthorityHandler}, add
 * implementation for
 * {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Order(Ordered.LOWEST_PRECEDENCE + 1)
@Named
public class OwnedExtEntityApiDataAuthorityHandler<T extends OwnedExtEntityImpl>
        extends OwnedEntityApiDataAuthorityHandler<T> {

    @Override
    public boolean support(Class<?> entityClass) {
        return OwnedExtEntityImpl.class.isAssignableFrom(entityClass);
    }

    @Override
    protected Predicate exchange(Class<T> entityClass, Method method, Predicate predicate) {
        final var primaryJob = SecurityUtilsExt.getPrimaryJob();
        if (primaryJob.isOrganizationChief()) {
            return QueryDslUtils.merge(predicate,
                    getBelongsToPredicate(entityClass, SecurityUtilsExt.getPrimaryOrganizationId()));
        }
        if (primaryJob.isDepartmentChief()) {
            return QueryDslUtils.merge(predicate,
                    getControlledByPredicate(entityClass, SecurityUtilsExt.getPrimaryDepartmentId()));
        }
        return super.exchange(entityClass, method, predicate);
    }

    private BooleanExpression getBelongsToPredicate(Class<T> entityClass, String primaryOrganizationId) {
        final var belongsToPath = (StringPath) QueryDslUtils.getPath(entityClass, "belongsTo");
        return belongsToPath.eq(primaryOrganizationId);
    }

    private BooleanExpression getControlledByPredicate(Class<T> entityClass, String primaryDepartmentId) {
        final var controlledBy = (StringPath) QueryDslUtils.getPath(entityClass, "controlledBy");
        return controlledBy.eq(primaryDepartmentId);
    }

    @Override
    public boolean isAuthorized(Method method, T entity) {
        final var ownedExt = (OwnedExtEntityImpl) entity;
        if (StringUtils.isBlank(ownedExt.getId())) {
            return true;
        } else {
            final var primaryJob = SecurityUtilsExt.getPrimaryJob();
            final var primaryDepartmentId = SecurityUtilsExt.getPrimaryDepartmentId();
            final var controlledBy = ownedExt.getControlledBy();
            if (primaryJob.isDepartmentChief() && StringUtils.equals(primaryDepartmentId, controlledBy)) {
                return true;
            }
            final var primaryOrganizationId = SecurityUtilsExt.getPrimaryOrganizationId();
            final var belongsTo = ownedExt.getBelongsTo();
            if (primaryJob.isOrganizationChief() && StringUtils.equals(primaryOrganizationId, belongsTo)) {
                return true;
            }
            return super.isAuthorized(method, entity);
        }
    }

}
