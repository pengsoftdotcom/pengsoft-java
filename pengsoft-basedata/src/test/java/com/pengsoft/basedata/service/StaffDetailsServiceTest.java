package com.pengsoft.basedata.service;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
public class StaffDetailsServiceTest {

    @Inject
    StaffDetailsService service;

    @Test
    void loadUserByUsername() {
        service.loadUserByUsername("1");
    }

}
