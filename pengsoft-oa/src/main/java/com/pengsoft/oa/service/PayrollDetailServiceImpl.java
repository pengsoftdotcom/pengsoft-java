package com.pengsoft.oa.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.repository.PayrollDetailRepository;
import com.pengsoft.oa.repository.PayrollRecordRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
    public PayrollDetail save(PayrollDetail target) {
        findOneByPayrollAndStaff(target.getPayroll(), target.getStaff()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("payroll", "exists", target.getPayroll().getId());
            }
        });
        return super.save(target);
    }

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
    public Optional<PayrollDetail> findOneByPayrollAndStaff(@NotNull PayrollRecord payroll, @NotNull Staff staff) {
        return getRepository().findOneByPayrollIdAndStaffId(payroll.getId(), staff.getId());
    }

    @Override
    public long countByPayroll(PayrollRecord payroll) {
        return getRepository().countByPayrollId(payroll.getId());
    }

    @Override
    public long countByPayrollAndConfirmedAtIsNotNull(PayrollRecord payroll) {
        return getRepository().countByPayrollIdAndConfirmedAtIsNotNull(payroll.getId());
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Direction.DESC, "payroll.year", "payroll.month");
    }

}
