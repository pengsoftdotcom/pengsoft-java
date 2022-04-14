package com.pengsoft.oa.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
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

    /**
     * Whether exists a {@link PayrollDetail} with the given payroll record year,
     * month and staff id.
     * 
     * @param year  The payroll record year
     * @param month The payroll record month
     * @param staff The {@link Staff}
     */
    boolean existsByPayrollYearAndPayrollMonthAndStaff(int year, int month, @NotNull Staff staff);

    /**
     * Returns the number of payroll details with given payroll record.
     * 
     * @param payroll The {@link PayrollRecord}.
     */
    long countByPayroll(@NotNull PayrollRecord payroll);

    /**
     * Returns the number of payroll details with given payroll record and
     * confirmedAt is not null.
     * 
     * @param payroll The payroll record id.
     */
    long countByPayrollAndConfirmedAtIsNotNull(@NotNull PayrollRecord payroll);

}
