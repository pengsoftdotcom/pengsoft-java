package com.pengsoft.basedata.service;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class StaffServiceTest {

    @Inject
    StaffService service;

    @Test
    void save() {
    }

}
