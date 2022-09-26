package com.pengsoft.ss.aspect;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.aspect.OwnedEntityApiDataAuthorityHandler;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 附件API数据权限处理器
 */
@Named
public class AssetApiDataAuthorityHandler extends OwnedEntityApiDataAuthorityHandler<Asset> {

    @Inject
    private ConstructionProjectService constructionProjectService;

    @Inject
    private StaffRepository staffRepository;

    @Override
    public boolean support(Class<?> entityClass) {
        return Asset.class.isAssignableFrom(entityClass);
    }

    @Override
    protected boolean isAuthorized(Method method, Asset entity) {
        final var departmentIds = getDepartmentIds();
        if (CollectionUtils.isNotEmpty(departmentIds)) {
            final var departmentId = staffRepository.findOneByPersonUserIdAndPrimaryTrue(entity.getCreatedBy())
                    .map(Staff::getDepartment).map(Department::getId).orElse(null);
            if (StringUtils.isNotBlank(departmentId)) {
                return CollectionUtils.containsAny(departmentIds, departmentId);
            }
        }
        return super.isAuthorized(method, entity);
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
