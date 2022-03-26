package com.pengsoft.oa.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.QPayrollRecord;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link PayrollRecord,} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PayrollRecordRepository
        extends EntityRepository<QPayrollRecord, PayrollRecord, String>, OwnedExtRepository {

    /**
     * Returns an {@link Optional} of a {@link PayrollRecord} with the given code
     * and
     * belongsTo.
     * 
     * @param code      The coding rule's code
     * @param belongsTo The coding rule's belongsTo
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<PayrollRecord> findOneByCodeAndBelongsTo(@NotBlank String code, String belongsTo);

}
