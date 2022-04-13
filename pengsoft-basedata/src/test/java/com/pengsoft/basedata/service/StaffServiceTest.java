package com.pengsoft.basedata.service;

import javax.inject.Inject;

import com.pengsoft.basedata.util.SecurityUtilsExt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class StaffServiceTest {

    @Inject
    StaffService service;

    @Test
    @WithUserDetails("15730470994")
    void findAllByDepartmentAndRoleCodes() {
        final var staffs = service.findAllByDepartmentAndRoleCodes(SecurityUtilsExt.getPrimaryDepartment(),
                "security_officer");
        System.out.println(staffs.size());
    }

}
