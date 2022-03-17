package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.domain.QCodingRule;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link CodingRule} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface CodingRuleRepository extends EntityRepository<QCodingRule, CodingRule, String>, OwnedExtRepository {

    /**
     * Returns an {@link Optional} of a {@link CodingRule} with the given entity and
     * belongsTo.
     * 
     * @param entity    The coding rule's entity
     * @param belongsTo The coding rule's belongsTo
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<CodingRule> findOneByEntityAndBelongsTo(@NotBlank String entity, String belongsTo);

}
