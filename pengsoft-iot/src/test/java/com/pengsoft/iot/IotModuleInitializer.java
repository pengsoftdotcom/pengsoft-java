package com.pengsoft.iot;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.iot.domain.Device;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.Product;
import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot" })
class IotModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(Device.class, Product.class, Group.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
