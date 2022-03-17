package com.pengsoft.acs;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.acs.domain.PersonFaceData;
import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot", "acs" })
class AcsModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(AccessRecord.class, PersonFaceData.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
