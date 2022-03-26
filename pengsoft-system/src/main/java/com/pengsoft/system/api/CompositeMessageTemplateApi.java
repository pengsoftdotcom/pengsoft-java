package com.pengsoft.system.api;

import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.CompositeMessageTemplate;
import com.pengsoft.system.service.CompositeMessageTemplateService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/api/system/composite-message-template" })
public class CompositeMessageTemplateApi
        extends EntityApi<CompositeMessageTemplateService, CompositeMessageTemplate, String> {
}
