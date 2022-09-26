package com.pengsoft.ss.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.service.SafetyTrainingParticipantService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The web api of {@link SafetyTrainingParticipant}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/ss/safety-training-participant")
public class SafetyTrainingParticipantApi extends
        EntityApi<SafetyTrainingParticipantService, SafetyTrainingParticipant, String> {

    @Inject
    private StaffService staffService;

    @TaskHandler(name = "safetyTrainingConfirmTaskHandler", finish = true)
    @PutMapping("confirm")
    public void confirm(@RequestParam("id") SafetyTrainingParticipant participant, String reason,
            @RequestParam(value = "status.id", required = false) @NotNull DictionaryItem status) {
        getService().confirm(participant, status, reason);
    }

    @Override
    public Page<SafetyTrainingParticipant> findPage(Predicate predicate, Pageable pageable) {
        final var root = QSafetyTrainingParticipant.safetyTrainingParticipant;
        if (SecurityUtils.hasAnyRole("worker")) {
            predicate = QueryDslUtils.merge(predicate, root.staff.id.eq(SecurityUtilsExt.getStaffId()));
        }
        return super.findPage(getQueryPredicate(predicate), pageable);
    }

    @GetMapping("statistic")
    public List<Map<String, Object>> statistic(
            @RequestParam(value = "project.id", required = false) List<String> projectIds, LocalDateTime startTime,
            LocalDateTime endTime) {
        return getService().statistic(projectIds, startTime, endTime);
    }

    private Predicate getQueryPredicate(Predicate predicate) {
        final var request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest();
        final var keyword = request.getParameter("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            final var root = QStaff.staff.person;
            final var staffIds = staffService.findAll(
                    root.name.contains(keyword)
                            .or(root.mobile.contains(keyword).or(root.identityCardNumber.contains(keyword))),
                    Sort.unsorted()).stream().map(Staff::getId).toList();
            predicate = QueryDslUtils.merge(predicate,
                    QSafetyTrainingParticipant.safetyTrainingParticipant.staff.id.in(staffIds));
        }
        return predicate;
    }

}
