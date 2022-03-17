package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
public class CodingRuleRepositoryTest {

    @Inject
    CodingRuleRepository repository;

    @Test
    void testFindOneByEntityAndBelongsTo() {
        repository.findOneByEntityAndBelongsTo("1", null);
    }
}
