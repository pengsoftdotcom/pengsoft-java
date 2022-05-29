package com.pengsoft.oa.excel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.service.PayrollDetailService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Excel reader for {@link PayrollDetailData}
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class PayrollDetailDataReadListener implements ReadListener<PayrollDetailData> {

    private PayrollDetailService service;

    private StaffRepository staffRepository;

    private PersonRepository personRepository;

    @Setter
    private PayrollRecord payroll;

    @Setter
    private Job job;

    @Getter
    private List<PayrollDetail> details = new ArrayList<>();

    @Inject
    private Exceptions exceptions;

    public PayrollDetailDataReadListener(
            PayrollDetailService payrollDetailService,
            StaffRepository staffRepository,
            PersonRepository personRepository) {
        this.service = payrollDetailService;
        this.staffRepository = staffRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void invoke(PayrollDetailData data, AnalysisContext context) {
        final var identityCardNumber = StringUtils.replace(data.getIdentityCardNumber(), "\s", "");
        if (StringUtils.isNotBlank(identityCardNumber)) {
            final var person = personRepository.findOneByIdentityCardNumber(identityCardNumber)
                    .orElseThrow(() -> exceptions.entityNotExists(Person.class, identityCardNumber));
            final var staff = staffRepository.findOneByPersonIdAndJobId(person.getId(), job.getId())
                    .orElseThrow(() -> exceptions.entityNotExists(Staff.class, person.getId()));
            final var detail = service.findOneByPayrollAndStaff(payroll, staff).orElse(new PayrollDetail());
            if (StringUtils.isBlank(detail.getId())) {
                detail.setPayroll(payroll);
                detail.setStaff(staff);
            }
            detail.setGrossPay(data.getGrossPay());
            detail.setNetPay(data.getNetPay());
            details.add(detail);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        payroll.getDetails().stream()
                .filter(source -> details.stream().noneMatch(target -> EntityUtils.equals(source, target)))
                .forEach(service::delete);
        service.save(details);
        payroll.setPaidCount(details.size());
        if (payroll.getSignedSheet() != null) {
            payroll.setConfirmedCount(payroll.getPaidCount());
        }
        payroll.setImportedAt(DateUtils.currentDateTime());
        payroll.setDetails(details);
        details = new ArrayList<>();
    }

}
