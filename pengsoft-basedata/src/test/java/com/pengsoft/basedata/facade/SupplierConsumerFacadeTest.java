package com.pengsoft.basedata.facade;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.basedata.service.OrganizationService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class SupplierConsumerFacadeTest {

    @Inject
    SupplierConsumerFacade facade;

    @Inject
    OrganizationService service;

    @Test
    void save() {
        final var optional = service.findOneByName("重庆鹏软科技有限公司");
        assertThrows(IllegalArgumentException.class, () -> optional
                .ifPresent(organization -> facade.save(new SupplierConsumer(organization, organization))));
    }

}
