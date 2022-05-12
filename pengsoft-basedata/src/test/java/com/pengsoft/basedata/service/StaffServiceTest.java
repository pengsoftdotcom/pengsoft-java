package com.pengsoft.basedata.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class StaffServiceTest {

    @Inject
    StaffService service;

    @Inject
    EntityManager entityManager;

    @Test
    @WithUserDetails("15730470994")
    void findAllByDepartmentAndRoleCodes() {
        final var staffs = service.findAllByDepartmentsAndRoleCodes(List.of(SecurityUtilsExt.getPrimaryDepartment()),
                List.of("security_officer"));
        System.out.println(staffs.size());
    }

    @Test
    void count() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        System.out.println(queryFactory.select(QStaff.staff.person.countDistinct()).from(QStaff.staff).fetchOne());
    }

}
