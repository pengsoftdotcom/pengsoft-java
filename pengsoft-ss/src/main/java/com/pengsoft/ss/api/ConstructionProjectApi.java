package com.pengsoft.ss.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.facade.ConstructionProjectFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

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

    @GetMapping("statistic-by-status")
    public List<Map<String, Object>> statisticByStatus() {
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER, ConstructionProject.ROL_OWNER_MANAGER,
                ConstructionProject.ROL_SU_MANAGER, ConstructionProject.ROL_BU_MANAGER)) {
            final var department = SecurityUtilsExt.getPrimaryDepartment();
            return getService().statisticByStatus(department);
        } else {
            return List.of();
        }
    }

}
