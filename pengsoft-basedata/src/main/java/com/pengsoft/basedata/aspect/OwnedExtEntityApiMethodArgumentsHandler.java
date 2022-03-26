package com.pengsoft.basedata.aspect;

import javax.inject.Named;

import com.pengsoft.basedata.domain.OwnedExt;
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.aspect.ApiMethodArgsDataAuthorityHandler;
import com.pengsoft.security.aspect.OwnedEntityApiMethodArgsDataAuthorityHandler;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.context.annotation.Primary;

/**
 * The implementer of {@link ApiMethodArgsDataAuthorityHandler}, add
 * implementation for
 * {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Named
public class OwnedExtEntityApiMethodArgumentsHandler extends OwnedEntityApiMethodArgsDataAuthorityHandler {

    @Override
    public boolean support(Class<?> entityClass) {
        if (OwnedExt.class.isAssignableFrom(entityClass)) {
            return true;
        } else {
            return super.support(entityClass);
        }
    }

    @Override
    protected Predicate exchange(final Class<? extends OwnedEntityImpl> entityClass, Authorized authorized,
            final Predicate predicate) {
        if (isReadable(authorized, entityClass) || hasAdminRole(entityClass)) {
            return predicate;
        } else {
            if (OwnedExtEntityImpl.class.isAssignableFrom(entityClass)) {
                final var primaryJob = SecurityUtilsExt.getPrimaryJob();
                if (primaryJob.isOrganizationChief()) {
                    return QueryDslUtils.merge(predicate,
                            getBelongsToPredicate(entityClass, SecurityUtilsExt.getPrimaryOrganizationId()));
                }
                if (primaryJob.isDepartmentChief()) {
                    return QueryDslUtils.merge(predicate,
                            getControlledByPredicate(entityClass, SecurityUtilsExt.getPrimaryDepartmentId()));
                }
            }
            return super.exchange(entityClass, authorized, predicate);
        }
    }

    private BooleanExpression getBelongsToPredicate(final Class<? extends OwnedEntityImpl> entityClass,
            final String primaryOrganizationId) {
        final var belongsToPath = (StringPath) QueryDslUtils.getPath(entityClass, "belongsTo");
        return belongsToPath.eq(primaryOrganizationId);
    }

    private BooleanExpression getControlledByPredicate(final Class<? extends OwnedEntityImpl> entityClass,
            final String primaryDepartmentId) {
        final var controlledBy = (StringPath) QueryDslUtils.getPath(entityClass, "controlledBy");
        return controlledBy.eq(primaryDepartmentId);
    }

    @Override
    public boolean isAuthorized(OwnedEntityImpl entity, Authorized authorized) {
        entity = getEntity(entity);
        final var entityClass = entity.getClass();
        if (StringUtils.isBlank(entity.getId()) || isWritable(authorized, entityClass) || hasAdminRole(entityClass)) {
            return true;
        } else {
            if (entity instanceof OwnedExtEntityImpl ownedExt) {
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
            }
            return super.isAuthorized(entity, authorized);
        }
    }

}
