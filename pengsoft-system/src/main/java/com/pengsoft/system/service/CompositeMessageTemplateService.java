package com.pengsoft.system.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.CompositeMessageTemplate;

public interface CompositeMessageTemplateService extends EntityService<CompositeMessageTemplate, String> {

    Optional<CompositeMessageTemplate> findOneByCode(@NotBlank String code);

}
