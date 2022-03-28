package com.pengsoft.ss.api;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.service.SafetyTrainingParticipantService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;

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

    @PutMapping("confirm")
    public void confirm(@RequestParam("id") SafetyTrainingParticipant participant,
            @RequestParam("status.id") DictionaryItem status, String reason) {
        getService().confirm(participant, status, reason);
    }

    @Authorized
    @PutMapping("confirm-mine")
    public void confirm(String id, String reason,
            @RequestParam(value = "status.id", required = false) @NotNull DictionaryItem status) {
        getService().findOne(id).ifPresent(participant -> getService().confirm(participant, status, reason));
    }

    @SneakyThrows
    @Authorized
    @GetMapping("find-one-of-mine")
    public SafetyTrainingParticipant findOneOfMine(String id) {
        return getService().findOne(id).orElse(SafetyTrainingParticipant.class.getDeclaredConstructor().newInstance());
    }

    @Authorized
    @GetMapping("find-page-of-mine")
    public Page<SafetyTrainingParticipant> findPageOfMine(Predicate predicate, Pageable pageable) {
        final var staff = SecurityUtilsExt.getStaff();
        final var root = QSafetyTrainingParticipant.safetyTrainingParticipant;
        predicate = QueryDslUtils.merge(predicate, root.staff.eq(staff));
        return findPage(predicate, pageable);
    }

}
