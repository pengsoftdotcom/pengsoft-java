package com.pengsoft.iot.service;

import javax.inject.Inject;

import com.pengsoft.iot.domain.QGroup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot" })
public class GroupServiceTest {

    @Inject
    GroupService service;

    @Test
    void findAll() {
        service.findAll(QGroup.group.id.in("39230d38-580d-4075-b8c2-7a8661213a36"), Sort.unsorted());
    }

}
