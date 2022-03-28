package com.pengsoft.ss.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.querydsl.jpa.JPAExpressions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "ss" })
class SafetyTrainingParticipantRepositoryTest {

    @Inject
    EntityManager entityManager;

    @Inject
    SafetyTrainingRepository safetyTrainingRepository;

    @Inject
    StaffRepository staffRepository;

    @Test
    @WithUserDetails("13500393502")
    void findOneByTrainingAndStaff() {
        final var staff = staffRepository.findOne(QStaff.staff.person.user.eq(SecurityUtils.getUser())).orElseThrow();
        final var participants = QSafetyTraining.safetyTraining.participants;
        final var participant = QSafetyTrainingParticipant.safetyTrainingParticipant;
        System.out.println(
                safetyTrainingRepository.count(JPAExpressions.selectOne().from(participants, participant)
                        .where(participant.staff.eq(staff)).exists()));
    }

}
