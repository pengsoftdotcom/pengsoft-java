package com.pengsoft.iot.repository;

import javax.inject.Inject;

import com.pengsoft.iot.domain.Group;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot" })
public class DeviceRepositoryTest {

    @Inject
    DeviceRepository repository;

    @Test
    void findPageByGroup() {
        repository.findPageByGroupAndName(new Group("39230d38-580d-4075-b8c2-7a8661213a36"), null,
                PageRequest.of(0, 10));
    }

}
