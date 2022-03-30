package com.pengsoft.ss.api;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.service.SafetyTrainingParticipantService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @TaskHandler(name = "safetyTrainingConfirmTaskHandler", finish = true)
    @PutMapping("confirm")
    public void confirm(@RequestParam("id") SafetyTrainingParticipant participant, String reason,
            @RequestParam(value = "status.id", required = false) @NotNull DictionaryItem status) {
        getService().confirm(participant, status, reason);
    }

    @Override
    public Page<SafetyTrainingParticipant> findPage(Predicate predicate, Pageable pageable) {
        final var staff = SecurityUtilsExt.getStaff();
        final var root = QSafetyTrainingParticipant.safetyTrainingParticipant;
        if (SecurityUtils.hasAnyRole("bu_manager'")) {
            predicate = QueryDslUtils.merge(predicate, root.training.project.buManager.eq(staff));
        }
        if (SecurityUtils.hasAnyRole("security_officer")) {
            predicate = QueryDslUtils.merge(predicate, root.training.trainer.eq(staff));
        }
        if (SecurityUtils.hasAnyRole("worker")) {
            predicate = QueryDslUtils.merge(predicate, root.staff.eq(staff));
        }
        return super.findPage(predicate, pageable);
    }

}
