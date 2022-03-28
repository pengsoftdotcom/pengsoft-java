package com.pengsoft.ss.service;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "ss" })
class SafetyTrainingServieTest {

    @Inject
    SafetyTrainingService safetyTrainingService;

    @Inject
    SafetyTrainingParticipantService safetyTrainingParticipantService;

    @Inject
    StaffService staffService;

    @Test
    @WithUserDetails("13500393502")
    void findPage() {
        final var user = SecurityUtils.getUser();
        final var staff = staffService.findOne(QStaff.staff.person.user.eq(user)).orElseThrow();
        final var participant = safetyTrainingParticipantService
                .findOne(QSafetyTrainingParticipant.safetyTrainingParticipant.staff.eq(staff)).orElseThrow();
        final var page = safetyTrainingService
                .findPage(QSafetyTraining.safetyTraining.participants.contains(participant), PageRequest.of(10, 10));
        System.out.println(page.getTotalElements());
    }

}
