package com.pengsoft.ss.api;

import java.util.List;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.ss.facade.ConstructionProjectFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if (SecurityUtils.hasAnyRole("ru_manager")) {
            final var staff = QStaff.staff;
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.select(root).leftJoin(root.ruManager, staff)
                    .where(staff.job.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        }

        if (SecurityUtils.hasAnyRole("bu_manager")) {
            predicate = QueryDslUtils.merge(predicate, root.buManager.id.eq(SecurityUtilsExt.getStaffId()));
        }
        return predicate;
    }

}
