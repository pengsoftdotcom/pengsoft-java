package com.pengsoft.system;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.domain.CompositeMessageTemplate;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.domain.EmailMessage;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.domain.Region;
import com.pengsoft.system.domain.SmsMessage;
import com.pengsoft.system.domain.SubscribeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system" })
class SystemModuleInitializer {

    @Inject
    RoleService service;

    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(CompositeMessageTemplate.class, EmailMessage.class, SmsMessage.class, InternalMessage.class,
                PushMessage.class, SubscribeMessage.class, Asset.class, Captcha.class, DictionaryType.class,
                DictionaryItem.class, Region.class).forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
