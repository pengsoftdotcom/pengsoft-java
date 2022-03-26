package com.pengsoft.oa.excel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.service.PayrollDetailService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.DateUtils;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Excel reader for {@link PayrollDetailData}
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class PayrollDetailDataReadListener implements ReadListener<PayrollDetailData> {

    private PayrollDetailService payrollDetailService;

    private StaffRepository staffRepository;

    private PersonRepository personRepository;

    @Getter
    @Setter
    private PayrollRecord payroll;

    @Getter
    @Setter
    private Job job;

    @Getter
    List<PayrollDetail> payrollDetails = new ArrayList<>();

    @Inject
    private Exceptions exceptions;

    public PayrollDetailDataReadListener(
            PayrollDetailService payrollDetailService,
            StaffRepository staffRepository,
            PersonRepository personRepository) {
        this.payrollDetailService = payrollDetailService;
        this.staffRepository = staffRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void invoke(PayrollDetailData data, AnalysisContext context) {
        final var detail = new PayrollDetail();
        BeanUtils.copyProperties(data, detail);
        detail.setPayroll(payroll);
        final var person = personRepository.findOneByIdentityCardNumber(data.getIdentityCardNumber())
                .orElseThrow(() -> exceptions.entityNotExists(data.getIdentityCardNumber()));
        final var staff = staffRepository.findOneByPersonIdAndJobId(person.getId(), job.getId())
                .orElseThrow(() -> exceptions.entityNotExists(data.getIdentityCardNumber()));
        detail.setStaff(staff);
        if (!payrollDetailService.existsByRecordCodeAndStaff(payroll.getCode(), staff)) {
            payrollDetails.add(detail);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        payrollDetailService.save(payrollDetails);
        payroll.setImportedAt(DateUtils.currentDateTime());
        final var paidCount = payrollDetailService.countByPayroll(payroll);
        payroll.setPaidCount(paidCount + payrollDetails.size());
        payrollDetails.clear();
    }

}
