package com.pengsoft.ss.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.Job;

import lombok.Setter;

/**
 * {@link SupervisionEngineerData} read lisener
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class SupervisionEngineerDataReadListener implements ReadListener<SupervisionEngineerData> {

    // @Inject
    // private PersonService personService;

    // @Inject
    // private StaffService staffService;

    @Setter
    private Job job;

    @Override
    public void invoke(SupervisionEngineerData data, AnalysisContext context) {
        // final var person = personService.findOneByMobile(data.getMobile()).orElse(new
        // Person());
        // if (StringUtils.isBlank(person.getId())) {
        // person.setName(data.getName());
        // person.setMobile(data.getMobile());
        // personService.save(person);
        // }

        // final var staff = staffService.findOneByPersonIdAndJobId(person.getId(),
        // job.getId()).orElse(new Staff());
        // if (StringUtils.isBlank(staff.getId())) {
        // staff.setPerson(person);
        // staff.setJob(job);
        // staffService.save(staff);
        // }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // nothing to do.
    }

}
