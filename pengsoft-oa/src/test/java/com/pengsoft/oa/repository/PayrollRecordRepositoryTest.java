package com.pengsoft.oa.repository;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.support.util.DateUtils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot", "acs", "oa" })
class PayrollRecordRepositoryTest {

    @Inject
    PayrollRecordRepository repository;

    @Test
    void statistic() {
        final var result = repository.statistic(List.of("5269719d-3bdb-4a48-a3fd-466f06fd5af4"),
                DateUtils.atStartOfCurrentYear(), DateUtils.atEndOfCurrentYear());
        System.out.println(result);
    }

}
