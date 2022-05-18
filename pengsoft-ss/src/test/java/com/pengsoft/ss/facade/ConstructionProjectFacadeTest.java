package com.pengsoft.ss.facade;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "ss" })
class ConstructionProjectFacadeTest {

    @Inject
    MessageSource messageSource;

    @Inject
    ConstructionProjectFacade facade;

    @Test
    void testGeneratePayrollRecords() {
        facade.generatePayrollRecords();
    }

}
