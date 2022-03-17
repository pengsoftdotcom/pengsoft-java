package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class PersonRepositoryTest {

    @Inject
    PersonRepository repository;

    @Test
    void findOneByUserId() {
        repository.findOneByUserId("1");
    }

}
