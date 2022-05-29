package com.pengsoft.oa.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.PayrollRecordConfirmPicture;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link PayrollRecord}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PayrollRecordConfirmPictureService extends EntityService<PayrollRecordConfirmPicture, String> {

    /**
     * Returns all {@link PayrollRecordConfirmPicture}s with the given payroll
     * record.
     * 
     * @param payrollRecord The {@link PayrollRecord}.
     */
    List<PayrollRecordConfirmPicture> findAllByPayrollRecord(@NotNull PayrollRecord payrollRecord);

}
