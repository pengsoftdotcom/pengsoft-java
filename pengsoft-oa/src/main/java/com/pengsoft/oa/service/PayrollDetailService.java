package com.pengsoft.oa.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link PayrollDetail}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0∏
 */
public interface PayrollDetailService extends EntityService<PayrollDetail, String> {

    /**
     * Confirm the payroll record has paid.
     * 
     * @param payrollDetail {@link PayrollDetail}
     */
    void confirm(@NotNull PayrollDetail payrollDetail);

}
