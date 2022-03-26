package com.pengsoft.ss.api;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.ss.service.SafetyTrainingService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Asset;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link SafetyTraining}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/ss/safety-training")
public class SafetyTrainingApi extends EntityApi<SafetyTrainingService, SafetyTraining, String> {

    @Inject
    private ConstructionProjectService projectService;

    @Messaging(builder = "safetyTrainingConfirmMessageBuilder")
    @PostMapping("save-and-submit")
    public void saveAndSubmit(@RequestBody SafetyTraining training) {
        getService().saveAndSubmit(training);
    }

    @Messaging(builder = "safetyTrainingConfirmMessageBuilder")
    @PutMapping("submit")
    public void submit(@RequestParam("id") SafetyTraining training) {
        getService().submit(training);
    }

    @PutMapping("start")
    public void start(@RequestParam("id") SafetyTraining training) {
        getService().start(training);
    }

    @PutMapping("end")
    public void end(@RequestParam("id") SafetyTraining training,
            @RequestParam(value = "file.id", required = false) List<Asset> files) {
        getService().end(training, files);
    }

    @Override
    public SafetyTraining findOne(@RequestParam(value = "id", required = false) SafetyTraining entity) {
        entity = super.findOne(entity);
        final var root = QConstructionProject.constructionProject;
        projectService
                .findOne(root.controlledBy.eq(SecurityUtilsExt.getPrimaryDepartmentId())
                        .and(root.belongsTo.eq(SecurityUtilsExt.getPrimaryOrganizationId())))
                .ifPresent(entity::setProject);
        final var job = SecurityUtilsExt.getPrimaryJob();
        if (job != null && StringUtils.equals(job.getName(), "安全员")) {
            entity.setTrainer(SecurityUtilsExt.getStaff());
        }
        if (entity.getProject() != null) {
            entity.setAddress(entity.getProject().getName() + "项目部");
        }
        return entity;
    }

}
