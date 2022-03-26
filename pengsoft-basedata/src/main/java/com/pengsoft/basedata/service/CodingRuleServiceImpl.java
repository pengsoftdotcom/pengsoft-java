package com.pengsoft.basedata.service;

import java.util.Optional;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.repository.CodingRuleRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link CodingRuleService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class CodingRuleServiceImpl extends EntityServiceImpl<CodingRuleRepository, CodingRule, String>
        implements CodingRuleService {

    @Override
    public CodingRule save(final CodingRule target) {
        findOneByEntity(target.getEntity()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("entity", "exists", target.getEntity());
            }
        });
        return super.save(target);
    }

    @Override
    public Optional<CodingRule> findOneByEntity(String entity) {
        return getRepository().findOneByEntityAndControlledByAndBelongsTo(entity,
                SecurityUtilsExt.getPrimaryDepartmentId(), SecurityUtilsExt.getPrimaryOrganizationId());
    }

}
