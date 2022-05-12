package com.pengsoft.basedata.service;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class JobServiceTest {

    @Inject
    JobService service;

    @Test
    void findAllByNameContains() {
        service.findAllByName("工区1工人");
    }

}
