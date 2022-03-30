package com.pengsoft.task;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.task.domain.Task;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "task" })
class TaskModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(Task.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
