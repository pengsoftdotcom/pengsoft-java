package com.pengsoft.ss.excel;

import javax.inject.Inject;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import lombok.Setter;

/**
 * {@link WorkerData} read lisener
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class WorkerDataReadListener implements ReadListener<WorkerData> {

    @Inject
    private PersonService personService;

    @Inject
    private StaffService staffService;

    @Setter
    private Job job;

    @Inject
    private JobService jobService;

    @Override
    public void invoke(WorkerData data, AnalysisContext context) {
        final var name = StringUtils.replace(data.getName(), "\s", "");
        final var mobile = StringUtils.replace(data.getMobile(), "\s", "");
        final var identityCardNumber = StringUtils.replace(data.getIdentityCardNumber(), "\s", "");
        final var extraJobName = StringUtils.replace(data.getExtraJobname(), "\s", "");
        if (StringUtils.isNotBlank(mobile)) {
            final var person = personService.findOneByMobile(data.getMobile()).orElse(new Person());
            if (StringUtils.isBlank(person.getId())) {
                person.setName(name);
                person.setMobile(mobile);
                person.setIdentityCardNumber(identityCardNumber);
                personService.save(person);
            }

            final var staff = staffService.findOneByPersonAndJob(person, job).orElse(new Staff());
            if (StringUtils.isBlank(staff.getId())) {
                staff.setPerson(person);
                staff.setJob(job);
                staffService.save(staff);
            }
            if (StringUtils.isNotBlank(extraJobName)) {
                jobService.findAllByName(extraJobName).stream().filter(
                        extraJob -> EntityUtils.equals(extraJob.getDepartment().getOrganization(),
                                staff.getOrganization()))
                        .findFirst().ifPresent(extraJob -> {
                            final var extraStaff = staffService.findOneByPersonAndJob(person, extraJob)
                                    .orElse(new Staff());
                            if (StringUtils.isBlank(extraStaff.getId())) {
                                extraStaff.setPerson(person);
                                extraStaff.setJob(extraJob);
                                staffService.save(extraStaff);
                            }
                        });
            }

        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // nothing to do.
    }

}
