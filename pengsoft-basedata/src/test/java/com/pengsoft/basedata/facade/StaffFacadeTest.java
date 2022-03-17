package com.pengsoft.basedata.facade;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.JobService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
@Transactional
class StaffFacadeTest {

    @Inject
    StaffFacade facade;

    @Inject
    JobService service;

    @Test
    @Rollback
    void save() {
        final var staff = new Staff();
        final var person = new Person();
        person.setName("张三");
        person.setMobile("18512345678");
        staff.setPerson(person);
        service.findAll().stream().findAny().ifPresent(staff::setJob);
        staff.setPrimary(true);
        facade.save(staff);
    }

}
