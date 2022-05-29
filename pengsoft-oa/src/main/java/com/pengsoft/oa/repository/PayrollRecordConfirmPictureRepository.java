package com.pengsoft.oa.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.PayrollRecordConfirmPicture;
import com.pengsoft.oa.domain.QPayrollRecordConfirmPicture;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link PayrollRecordConfirmPicture} based on JPA
 *
 * @author peng.dang@pengsoft.com∏
 * @since 1.0.0
 */
@Repository
public interface PayrollRecordConfirmPictureRepository
        extends EntityRepository<QPayrollRecordConfirmPicture, PayrollRecordConfirmPicture, String>,
        OwnedExtRepository {

    /**
     * Returns all {@link PayrollRecordConfirmPicture}s with the given payroll
     * record id.
     * 
     * @param payrollId The payroll record id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<PayrollRecordConfirmPicture> findAllByPayrollId(@NotBlank String payrollId);

}
