package com.pengsoft.basedata;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.AuthenticationRecord;
import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.Rank;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class BasedataModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(AuthenticationRecord.class, BusinessLicense.class, IdentityCard.class, Department.class, Job.class,
                Organization.class, Person.class, Post.class, Rank.class, Staff.class, SupplierConsumer.class,
                CodingRule.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
