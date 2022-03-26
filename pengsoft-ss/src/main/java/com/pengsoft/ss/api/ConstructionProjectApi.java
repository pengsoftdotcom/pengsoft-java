package com.pengsoft.ss.api;

import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.facade.ConstructionProjectFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

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

}
