package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.domain.QCodingRule;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link CodingRule} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface CodingRuleRepository extends EntityRepository<QCodingRule, CodingRule, String>, OwnedExtRepository {

    /**
     * Returns an {@link Optional} of a {@link CodingRule} with the given entity and
     * belongsTo.
     * 
     * @param entity       The coding rule entity
     * @param controlledBy The coding rule controlledBy
     * @param belongsTo    The coding rule belongsTo
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<CodingRule> findOneByEntityAndControlledByAndBelongsTo(@NotBlank String entity, String controlledBy,
            String belongsTo);

}
