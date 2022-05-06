package com.pengsoft.ss.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.type.MapType;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.ss.facade.SafetyTrainingFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The web api of {@link SafetyTraining}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/ss/safety-training")
public class SafetyTrainingApi extends EntityApi<SafetyTrainingFacade, SafetyTraining, String> {

    public static final String ROL_BU_MANAGER = "bu_manager";

    public static final String ROL_SECURITY_OFFICER = "security_officer";

    public static final String ROL_WORKER = "worker";

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
        if (StringUtils.isBlank(training.getId())) {
            if (StringUtils.isBlank(training.getSubject())) {
                training.setSubject("例行安全教育培训");
            }
            training.setTrainer(SecurityUtilsExt.getStaff());
        }
        Map<String, Object> result = objectMapper.convertValue(training, type);
        result.put("files", training.getFiles().stream().map(SafetyTrainingFile::getFile).toList());
        return result;
    }

    @Override
    public Page<SafetyTraining> findPage(Predicate predicate, Pageable pageable) {
        final var root = QSafetyTraining.safetyTraining;
        predicate = getAuthorityPredicate(predicate, root);
        predicate = getQueryPredicate(predicate, root);
        return super.findPage(predicate, pageable);
    }

    private Predicate getQueryPredicate(Predicate predicate, final QSafetyTraining root) {
        final var request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        final var startTime = request.getParameter("startTime");
        final var endTime = request.getParameter("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            predicate = QueryDslUtils.merge(predicate, root.submittedAt.between(DateUtils.parseDateTime(startTime),
                    DateUtils.parseDateTime(endTime)));
        }
        return predicate;
    }

    private Predicate getAuthorityPredicate(Predicate predicate, QSafetyTraining root) {
        final var staff = SecurityUtilsExt.getStaff();
        final var qStaff = QStaff.staff;

        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            predicate = QueryDslUtils.merge(predicate,
                    JPAExpressions.select(root).leftJoin(root.project.ruManager, qStaff)
                            .where(qStaff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            predicate = QueryDslUtils.merge(predicate,
                    JPAExpressions.select(root).leftJoin(root.project.ownerManager, qStaff)
                            .where(qStaff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER,
                ConstructionProject.ROL_SUPERVISION_ENGINEER)) {
            predicate = QueryDslUtils.merge(predicate,
                    JPAExpressions.select(root).leftJoin(root.project.suManager, qStaff)
                            .where(qStaff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_BU_MANAGER,
                ConstructionProject.ROL_QUALITY_INSPECTOR,
                ConstructionProject.ROL_SECURITY_OFFICER)) {
            predicate = QueryDslUtils.merge(predicate,
                    JPAExpressions.select(root).leftJoin(root.project.buManager, qStaff)
                            .where(qStaff.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())).exists());
        } else if (SecurityUtils.hasAnyRole(ROL_WORKER)) {
            final var participants = QSafetyTraining.safetyTraining.participants;
            final var participant = QSafetyTrainingParticipant.safetyTrainingParticipant;
            predicate = QueryDslUtils.merge(predicate, JPAExpressions.selectOne().from(participants, participant)
                    .where(participant.staff.id.eq(staff.getId())).exists());
        } else if (!SecurityUtils.hasAnyRole(Role.ADMIN)) {
            predicate = Expressions.FALSE.isTrue();
        }
        return predicate;
    }

    @GetMapping("get-trained-days")
    public List<Map<String, Object>> getTrainedDays(@RequestParam("project.id") List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().getTrainedDays(projectIds, startTime, endTime);
    }

    @GetMapping("statistic")
    public List<Map<String, Object>> statistic(@RequestParam("project.id") List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().statistic(projectIds, startTime, endTime);
    }

    @GetMapping("statistic-by-trainer")
    public List<Map<String, Object>> statisticByTrainer(@RequestParam("project.id") List<String> projectIds,
            @RequestParam("trainer.id") List<String> trainerIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().statisticByTrainer(projectIds, trainerIds, startTime, endTime);
    }

}
