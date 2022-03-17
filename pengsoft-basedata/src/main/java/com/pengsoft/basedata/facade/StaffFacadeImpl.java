package com.pengsoft.basedata.facade;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link StaffFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class StaffFacadeImpl extends EntityFacadeImpl<StaffService, Staff, String> implements StaffFacade {

    @Inject
    private PersonService personService;

    @Override
    public Staff save(final Staff staff) {
        final var person = personService.findOneByMobile(staff.getPerson().getMobile()).orElse(staff.getPerson());
        if (StringUtils.isNotBlank(person.getId())) {
            BeanUtils.copyProperties(staff.getPerson(), person, "id", "mobile", "user", "version");
        }
        staff.setPerson(personService.save(person));
        return super.save(staff);
    }

    @Override
    public void setPrimaryJob(final Person person, final Job job) {
        getService().setPrimaryJob(person, job);
    }

    @Override
    public Optional<Staff> findOneByPersonAndPrimaryTrue(final Person person) {
        return getService().findOneByPersonAndPrimaryTrue(person);
    }

    @Override
    public List<Staff> findAllByPerson(final Person person) {
        return getService().findAllByPerson(person);
    }

    @Override
    public List<Staff> findAllByJobIn(final List<Job> jobs) {
        return getService().findAllByJobIn(jobs);
    }

}
