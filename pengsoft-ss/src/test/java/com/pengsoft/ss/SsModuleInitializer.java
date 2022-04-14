package com.pengsoft.ss;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QualityCheck;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class SsModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(ConstructionProject.class, SafetyTraining.class, SafetyTrainingParticipant.class, SafetyCheck.class,
                QualityCheck.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
