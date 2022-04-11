package com.pengsoft.oa.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot", "acs", "oa" })
class ContractRepositoryTest {

    @Inject
    ContractRepository repository;

    @Test
    void statisticByDepartmentId() {
        List<Map<String, Object>> result = repository
                .statisticByDepartmentId(List.of("f595b1bc-219b-4bbf-8184-fcdfbb06d14e"));
        System.out.println(result);
    }

}
