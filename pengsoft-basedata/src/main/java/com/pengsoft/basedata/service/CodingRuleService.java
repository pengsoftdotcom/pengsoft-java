package com.pengsoft.basedata.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link CodingRule}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface CodingRuleService extends EntityService<CodingRule, String> {

    /**
     * Returns an {@link Optional} of a {@link CodingRule} with the given entity.
     * 
     * @param entity The coding rule's entity
     */
    Optional<CodingRule> findOneByEntity(@NotBlank String entity);

}
