package com.pengsoft.oa.repository;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.QPayrollDetail;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link PayrollDetail} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PayrollDetailRepository
        extends EntityRepository<QPayrollDetail, PayrollDetail, String>, OwnedExtRepository {

    /**
     * Whether exists a {@link PayrollDetail} with the given payroll record code and
     * staff id.
     * 
     * @param payrollCode The payroll record code
     * @param staffId     The staff id
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    boolean existsByPayrollCodeAndStaffId(@NotBlank String payrollCode, @NotBlank String staffId);

    /**
     * Returns the number of payroll details with given payroll record id.
     * 
     * @param payrollId The payroll record id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByPayrollId(@NotBlank String payrollId);

    /**
     * Returns the number of payroll details with given payroll record id and
     * confirmedAt is not null.
     * 
     * @param payrollId The payroll record id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByPayrollIdAndConfirmedAtIsNotNull(@NotBlank String payrollId);

}
