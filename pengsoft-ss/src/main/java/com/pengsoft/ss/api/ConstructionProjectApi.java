package com.pengsoft.ss.api;

import java.util.List;
import java.util.Map;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.ss.facade.ConstructionProjectFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The web api of {@link ConstructionProject}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/ss/construction-project")
public class ConstructionProjectApi extends EntityApi<ConstructionProjectFacade, ConstructionProject, String> {

    @PostMapping("import-data")
    public void importData(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            getService().importData(file);
        }
    }

    @Override
    public Page<ConstructionProject> findPage(Predicate predicate, Pageable pageable) {
        return super.findPage(getPredicate(predicate), pageable);
    }

    @Override
    public List<ConstructionProject> findAll(Predicate predicate, Sort sort) {
        return super.findAll(getPredicate(predicate), sort);
    }

    private Predicate getPredicate(Predicate predicate) {
        final var root = QConstructionProject.constructionProject;
        final var staff = QStaff.staff;
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.select(root).leftJoin(root.ruManager, staff)
                    .where(staff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.select(root).leftJoin(root.ownerManager, staff)
                    .where(staff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER,
                ConstructionProject.ROL_SUPERVISION_ENGINEER)) {
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.select(root).leftJoin(root.suManager, staff)
                    .where(staff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_BU_MANAGER,
                ConstructionProject.ROL_SECURITY_OFFICER)) {
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.select(root).leftJoin(root.buManager, staff)
                    .where(staff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (!SecurityUtils.hasAnyRole(Role.ADMIN)) {
            predicate = Expressions.FALSE.isTrue();
        }
        return predicate;
    }

    @GetMapping("statistic-by-status")
    public List<Map<String, Object>> statisticByStatus() {
        return getService().statisticByStatus();
    }

}
