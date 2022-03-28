package com.pengsoft.oa.service;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.repository.PayrollDetailRepository;
import com.pengsoft.oa.repository.PayrollRecordRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PayrollDetail} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PayrollDetailServiceImpl extends EntityServiceImpl<PayrollDetailRepository, PayrollDetail, String>
        implements PayrollDetailService {

    @Inject
    private PayrollRecordRepository payrollRecordRepository;

    @Override
    public void confirm(PayrollDetail payrollDetail) {
        if (payrollDetail.getConfirmedAt() != null) {
            throw new BusinessException("payroll.confirm.already");
        }

        payrollDetail.setConfirmedAt(DateUtils.currentDateTime());
        payrollDetail.setConfirmedBy(SecurityUtils.getUserId());
        save(payrollDetail);

        final var payroll = payrollDetail.getPayroll();
        final var confirmedCount = countByPayrollAndConfirmedAtIsNotNull(payroll);
        payroll.setConfirmedCount(confirmedCount);
        payrollRecordRepository.save(payroll);
    }

    @Override
    public boolean existsByRecordCodeAndStaff(String recordCode, Staff staff) {
        return getRepository().existsByPayrollCodeAndStaffId(recordCode, staff.getId());
    }

    @Override
    public long countByPayroll(PayrollRecord payroll) {
        return getRepository().countByPayrollId(payroll.getId());
    }

    @Override
    public long countByPayrollAndConfirmedAtIsNotNull(PayrollRecord payroll) {
        return getRepository().countByPayrollIdAndConfirmedAtIsNotNull(payroll.getId());
    }

}
