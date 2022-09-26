package com.pengsoft.ss.aspect;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.aspect.OwnedExtEntityApiDataAuthorityHandler;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 安全检查API数据权限处理器
 */
@Named
public class ContractApiDataAuthorityHandler<T extends OwnedExtEntityImpl>
        extends OwnedExtEntityApiDataAuthorityHandler<T> {

    @Inject
    private ConstructionProjectService constructionProjectService;

    @Override
    public boolean support(Class<?> entityClass) {
        return Contract.class.isAssignableFrom(entityClass);
    }

    @Override
    protected Predicate exchange(Class<T> entityClass, Method method, Predicate predicate) {
        final var departmentIds = getDepartmentIds();
        if (CollectionUtils.isNotEmpty(departmentIds)) {
            return QueryDslUtils.merge(predicate,
                    ((StringPath) QueryDslUtils.getPath(entityClass, "controlledBy")).in(departmentIds));
        }
        return super.exchange(entityClass, method, predicate);
    }

    @Override
    public boolean isAuthorized(Method method, T entity) {
        final var departmentIds = getDepartmentIds();
        if (CollectionUtils.isNotEmpty(departmentIds)) {
            return CollectionUtils.containsAny(departmentIds, entity.getControlledBy());
        } else {
            return super.isAuthorized(method, entity);
        }
    }

    private List<String> getDepartmentIds() {
        final var projectId = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest().getParameter("project.id");
        List<ConstructionProject> projects = List.of();
        final var staff = SecurityUtilsExt.getStaff();
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            projects = constructionProjectService.findAllByRuManager(staff);
        }
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            projects = constructionProjectService.findAllByOwnerManager(staff);
        }
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER)) {
            projects = constructionProjectService.findAllBySuManager(staff);
        }
        return projects.stream()
                .filter(project -> StringUtils.isBlank(projectId) || project.getId().equals(projectId))
                .map(ConstructionProject::getBuManager).map(Staff::getDepartment).map(Department::getId)
                .toList();
    }

}
