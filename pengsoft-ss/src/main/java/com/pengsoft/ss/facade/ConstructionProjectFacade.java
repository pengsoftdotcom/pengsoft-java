package com.pengsoft.ss.facade;

import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.facade.EntityFacade;

import org.springframework.web.multipart.MultipartFile;

/**
 * The facade interface of {@link ConstructionProject}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ConstructionProjectFacade
        extends EntityFacade<ConstructionProjectService, ConstructionProject, String>, ConstructionProjectService {

    /**
     * Import construction project and related data.
     * 
     * @param file {@link MultipartFile}
     */
    void importData(MultipartFile file);

    /**
     * 发薪
     */
    void generatePayrollRecords();

}
