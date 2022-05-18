package com.pengsoft.basedata.repository;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class StaffRepositoryTest {

    @Inject
    StaffRepository repository;

    @Test
    void findAllByJobIn() {
        repository.findAllByJobIdIn(List.of("1"));
    }

    @Test
    void findOneByPersonIdAndJobId() {
        repository.findOneByPersonIdAndJobId("1", "1");
    }

}
