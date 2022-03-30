package com.pengsoft.ss.api;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.type.MapLikeType;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.ss.domain.QSafetyCheck;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.domain.SafetyCheckFile;
import com.pengsoft.ss.facade.SafetyCheckFacade;
import com.pengsoft.ss.repository.SafetyCheckFileRepository;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

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
 * The web api of {@link SafetyCheck}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/ss/safety-check")
public class SafetyCheckApi extends EntityApi<SafetyCheckFacade, SafetyCheck, String> {

    @Inject
    private ConstructionProjectService projectService;

    @Inject
    private StaffService staffService;

    @Inject
    private SafetyCheckFileRepository safetyCheckFileRepository;

    @Inject
    private DictionaryItemService dictionaryItemService;

    private ObjectMapper objectMapper;

    private MapLikeType type;

    public SafetyCheckApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.type = this.objectMapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class);
    }

    @TaskHandler(name = "safetyCheckRiskTaskHandler", create = true)
    @Messaging(builder = "safetyCheckHandleMessageBuilder")
    @PostMapping("submit")
    public void submit(@RequestBody SafetyCheck check,
            @RequestParam(value = "asset.id", required = false) @NotEmpty List<Asset> submitFiles) {
        getService().submit(check, submitFiles);
    }

    @TaskHandler(name = "safetyCheckRiskTaskHandler", finish = true)
    @PutMapping("handle")
    public void handle(@RequestParam("id") SafetyCheck check, @NotBlank String result,
            @RequestParam(value = "asset.id", required = false) @NotEmpty List<Asset> handleFiles) {
        getService().handle(check, result, handleFiles);
    }

    @GetMapping("find-one-with-files")
    public Map<String, Object> findOneWithFiles(@RequestParam(value = "id", required = false) SafetyCheck entity) {
        final var check = super.findOne(entity);
        if (check.getStatus() == null) {
            dictionaryItemService.findOneByTypeCodeAndParentAndCode("safety_check_status", null, "safe")
                    .ifPresent(check::setStatus);
        }
        final var job = SecurityUtilsExt.getPrimaryJob();
        if (job != null && StringUtils.equals(job.getName(), "安全员")) {
            check.setChecker(SecurityUtilsExt.getStaff());
            staffService.findOne(QStaff.staff.job.id.eq(job.getParent().getId()))
                    .ifPresent(staff -> projectService
                            .findOne(QConstructionProject.constructionProject.buManager.id.eq(staff.getId()))
                            .ifPresent(check::setProject));
        }
        if (job != null && StringUtils.equals(job.getName(), "监理工程师")) {
            check.setChecker(SecurityUtilsExt.getStaff());
            staffService.findOne(QStaff.staff.job.id.eq(job.getParent().getId()))
                    .ifPresent(staff -> projectService
                            .findOne(QConstructionProject.constructionProject.suManager.id.eq(staff.getId()))
                            .ifPresent(check::setProject));
        }
        Map<String, Object> result = objectMapper.convertValue(check, type);
        if (StringUtils.isNotBlank(check.getId())) {
            result.put("submitFiles", safetyCheckFileRepository.findAllByCheckIdAndTypeCode(check.getId(), "submit")
                    .stream().map(SafetyCheckFile::getFile).toList());
            result.put("handleFiles", safetyCheckFileRepository.findAllByCheckIdAndTypeCode(check.getId(), "handle")
                    .stream().map(SafetyCheckFile::getFile).toList());
        }
        return result;
    }

    @Override
    public Page<SafetyCheck> findPage(Predicate predicate, Pageable pageable) {
        final var staff = SecurityUtilsExt.getStaff();
        final var root = QSafetyCheck.safetyCheck;
        if (SecurityUtils.hasAnyRole("bu_manager'")) {
            predicate = QueryDslUtils.merge(predicate, root.project.buManager.id.eq(staff.getId()));
        }
        if (SecurityUtils.hasAnyRole("security_officer", "supervision_engineer")) {
            predicate = QueryDslUtils.merge(predicate, root.checker.id.eq(staff.getId()));
        }
        return super.findPage(predicate, pageable);
    }

    @DeleteMapping("delete-file-by-asset")
    public void deleteFileByAsset(@RequestParam(value = "id", required = false) SafetyCheck check,
            @RequestParam("asset.id") Asset asset) {
        getService().deleteFileByAsset(check, asset);
    }

}
