package com.pengsoft.ss.excel;

import javax.inject.Inject;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.service.StaffService;

import org.apache.commons.lang3.StringUtils;

import lombok.Setter;

/**
 * {@link SecurityOfficerData} read lisener
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class SecurityOfficerDataReadListener implements ReadListener<SecurityOfficerData> {

    @Inject
    private PersonService personService;

    @Inject
    private StaffService staffService;

    @Setter
    private Job job;

    @Override
    public void invoke(SecurityOfficerData data, AnalysisContext context) {
        final var name = StringUtils.replace(data.getName(), "\s", "");
        final var mobile = StringUtils.replace(data.getMobile(), "\s", "");
        if (StringUtils.isNotBlank(data.getMobile())) {
            final var person = personService.findOneByMobile(mobile).orElse(new Person());
            if (StringUtils.isBlank(person.getId())) {
                person.setName(name);
                person.setMobile(mobile);
                personService.save(person);
            }

            final var staff = staffService.findOneByPersonAndJob(person, job).orElse(new Staff());
            if (StringUtils.isBlank(staff.getId())) {
                staff.setPerson(person);
                staff.setJob(job);
                staffService.save(staff);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // nothing to do.
    }

}
