package com.pengsoft.ss.api;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.type.MapType;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.ss.facade.SafetyTrainingFacade;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class SafetyTrainingApi extends EntityApi<SafetyTrainingFacade, SafetyTraining, String> {

    @Inject
    private ConstructionProjectService projectService;

    @Inject
    private StaffService staffService;

    private ObjectMapper objectMapper;

    private MapType type;

    public SafetyTrainingApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        type = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    }

    @TaskHandler(name = "safetyTrainingConfirmTaskHandler", create = true)
    @Messaging(builder = "safetyTrainingConfirmMessageBuilder")
    @PostMapping("save-and-submit")
    public void saveAndSubmit(@RequestBody SafetyTraining training) {
        getService().saveAndSubmit(training);
    }

    @TaskHandler(name = "safetyTrainingConfirmTaskHandler", create = true)
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
            @RequestParam(value = "file.id", required = false) @NotEmpty List<Asset> files) {
        getService().end(training, files);
    }

    @DeleteMapping("delete-file-by-asset")
    public void deletePictureByAsset(@RequestParam(value = "id", required = false) SafetyTraining training,
            @RequestParam("asset.id") Asset asset) {
        getService().deleteFileByAsset(training, asset);
    }

    @GetMapping("find-one-with-files")
    public Map<String, Object> findOneWithFiles(@RequestParam(value = "id", required = false) SafetyTraining entity) {
        final var training = super.findOne(entity);
        final var job = SecurityUtilsExt.getPrimaryJob();
        if (job != null && StringUtils.equals(job.getName(), "安全员")) {
            training.setTrainer(SecurityUtilsExt.getStaff());
            staffService.findOne(QStaff.staff.job.id.eq(job.getParent().getId()))
                    .ifPresent(staff -> projectService
                            .findOne(QConstructionProject.constructionProject.buManager.id.eq(staff.getId()))
                            .ifPresent(training::setProject));
        }
        if (training.getProject() != null) {
            training.setAddress(training.getProject().getName() + "项目部");
        }
        Map<String, Object> result = objectMapper.convertValue(training, type);
        result.put("files", training.getFiles().stream().map(SafetyTrainingFile::getFile).toList());
        return result;
    }

    @Override
    public Page<SafetyTraining> findPage(Predicate predicate, Pageable pageable) {
        final var staff = SecurityUtilsExt.getStaff();
        final var root = QSafetyTraining.safetyTraining;
        if (SecurityUtils.hasAnyRole("bu_manager'")) {
            predicate = QueryDslUtils.merge(predicate, root.project.buManager.id.eq(staff.getId()));
        }
        if (SecurityUtils.hasAnyRole("security_officer")) {
            predicate = QueryDslUtils.merge(predicate, root.trainer.id.eq(staff.getId()));
        }
        if (SecurityUtils.hasAnyRole("worker")) {
            final var participants = QSafetyTraining.safetyTraining.participants;
            final var participant = QSafetyTrainingParticipant.safetyTrainingParticipant;
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.selectOne().from(participants, participant)
                    .where(participant.staff.id.eq(staff.getId())).exists());
        }
        return super.findPage(predicate, pageable);
    }

}
