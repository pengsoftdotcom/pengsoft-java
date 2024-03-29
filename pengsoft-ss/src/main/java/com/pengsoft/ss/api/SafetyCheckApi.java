package com.pengsoft.ss.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.type.MapLikeType;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.ss.domain.QSafetyCheck;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.domain.SafetyCheckFile;
import com.pengsoft.ss.facade.SafetyCheckFacade;
import com.pengsoft.ss.repository.SafetyCheckFileRepository;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.DateUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @TaskHandler(name = "safetyCheckRiskTaskHandler", delete = true)
    @Override
    public void delete(Predicate predicate) {
        super.delete(predicate);
    }

    @TaskHandler(name = "safetyCheckRiskTaskHandler", create = true)
    @PutMapping("reduce")
    public void reduce(@RequestParam("id") SafetyCheck check) {
        getService().reduce(check);
    }

    @GetMapping("find-one-with-files")
    public Map<String, Object> findOneWithFiles(@RequestParam(value = "id", required = false) SafetyCheck entity) {
        final var check = super.findOne(entity);
        if (StringUtils.isBlank(check.getId())) {
            dictionaryItemService.findOneByTypeCodeAndParentAndCode("safety_check_status", null, "safe")
                    .ifPresent(check::setStatus);
            check.setChecker(SecurityUtilsExt.getStaff());
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
        final var root = QSafetyCheck.safetyCheck;
        predicate = getQueryPredicate(predicate, root);
        return super.findPage(predicate, pageable);
    }

    private Predicate getQueryPredicate(Predicate predicate, final QSafetyCheck root) {
        final var request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        final var startTime = request.getParameter("startTime");
        final var endTime = request.getParameter("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            predicate = QueryDslUtils.merge(predicate, root.submittedAt.between(DateUtils.parseDateTime(startTime),
                    DateUtils.parseDateTime(endTime)));
        }
        final var handled = request.getParameter("handled");
        if (StringUtils.isNotBlank(handled)) {
            predicate = QueryDslUtils.merge(predicate,
                    Boolean.parseBoolean(handled) ? root.handledAt.isNotNull() : root.handledAt.isNull());
        }
        return predicate;
    }

    @DeleteMapping("delete-file-by-asset")
    public void deleteFileByAsset(@RequestParam(value = "id", required = false) SafetyCheck check,
            @RequestParam("asset.id") Asset asset) {
        getService().deleteFileByAsset(check, asset);
    }

    @GetMapping("get-checked-days")
    public List<Map<String, Object>> getCheckedDays(@RequestParam("project.id") List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().getCheckedDays(projectIds, startTime, endTime);
    }

    @GetMapping("statistic")
    public List<Map<String, Object>> statistic(@RequestParam("project.id") List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().statistic(projectIds, startTime, endTime);
    }

    @GetMapping("statistic-by-checker")
    public List<Map<String, Object>> statisticByChecker(@RequestParam("project.id") List<String> projectIds,
            @RequestParam("checker.id") List<String> checkerIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().statisticByChecker(projectIds, checkerIds, startTime, endTime);
    }

    @GetMapping("find-all-unchecked-or-unhandled-dates")
    public List<Map<String, Object>> findAllUncheckedOrUnhandledDates(
            @RequestParam("project.id") List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().findAllUncheckedOrUnhandledDates(projectIds, startTime, endTime);
    }

    @GetMapping("statistic-by-day")
    public List<Map<String, Object>> statisticByDay(
            @RequestParam("project.id") List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().statisticByDay(projectIds, startTime, endTime);
    }

}
