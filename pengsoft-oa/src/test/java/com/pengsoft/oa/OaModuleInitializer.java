package com.pengsoft.oa;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot", "acs", "oa" })
class OaModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(Contract.class, PayrollRecord.class, PayrollDetail.class, AttendanceRecord.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
