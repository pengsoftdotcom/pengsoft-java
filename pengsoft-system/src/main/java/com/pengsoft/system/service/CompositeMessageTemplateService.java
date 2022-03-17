package com.pengsoft.system.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.CompositeMessageTemplate;

/**
 * The service interface of {@link CompositeMessageTemplate}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface CompositeMessageTemplateService extends EntityService<CompositeMessageTemplate, String> {

    /**
     * Returns an {@link Optional} of a {@link CompositeMessageTemplate} with the
     * given code.
     *
     * @param code {@link CompositeMessageTemplate}'s code
     */
    Optional<CompositeMessageTemplate> findOneByCode(@NotBlank String code);

}
