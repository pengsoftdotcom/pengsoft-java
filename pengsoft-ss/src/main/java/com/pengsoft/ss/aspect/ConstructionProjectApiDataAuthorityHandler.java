package com.pengsoft.ss.aspect;

import java.lang.reflect.Method;

import javax.inject.Named;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.aspect.AbstractApiDataAuthorityHandler;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

/**
 * 工程项目API接口数据权限处理器
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class ConstructionProjectApiDataAuthorityHandler extends AbstractApiDataAuthorityHandler<ConstructionProject> {

    @Override
    public boolean support(Class<?> entityClass) {
        return ConstructionProject.class.isAssignableFrom(entityClass);
    }

    @Override
    protected Predicate exchange(Class<ConstructionProject> entityClass, Method method, Predicate predicate) {
        final var departmentId = SecurityUtilsExt.getPrimaryDepartmentId();
        final var root = QConstructionProject.constructionProject;
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            predicate = QueryDslUtils.merge(predicate, root.ruManager.department.id.eq(departmentId));
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            predicate = QueryDslUtils.merge(predicate, root.ownerManager.department.id.eq(departmentId));
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER,
                ConstructionProject.ROL_SUPERVISION_ENGINEER)) {
            predicate = QueryDslUtils.merge(predicate, root.suManager.department.id.eq(departmentId));
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_BU_MANAGER,
                ConstructionProject.ROL_SECURITY_OFFICER)) {
            predicate = QueryDslUtils.merge(predicate, root.buManager.department.id.eq(departmentId));
        } else {
            predicate = Expressions.FALSE.isTrue();
        }
        return predicate;
    }

    @Override
    protected boolean isAuthorized(Method method, ConstructionProject project) {
        final var departmentId = SecurityUtilsExt.getPrimaryDepartmentId();
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            return project.getRuManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            return project.getOwnerManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER,
                ConstructionProject.ROL_SUPERVISION_ENGINEER)) {
            return project.getSuManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_BU_MANAGER,
                ConstructionProject.ROL_SECURITY_OFFICER)) {
            return project.getBuManager().getDepartment().getId().equals(departmentId);
        }
        return false;
    }

}
