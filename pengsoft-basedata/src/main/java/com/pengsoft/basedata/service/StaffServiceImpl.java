package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.QJobRole;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.jpa.JPAExpressions;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link StaffService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class StaffServiceImpl extends EntityServiceImpl<StaffRepository, Staff, String> implements StaffService {

    @Override
    public Staff save(final Staff staff) {
        final var leftQuantity = staff.getJob().getQuantity() - getRepository().countByJobId(staff.getJob().getId())
                - (StringUtils.isBlank(staff.getId()) ? 1 : 0);
        if (leftQuantity < 0) {
            throw new BusinessException("staff.save.exceeded", staff.getJob().getName());
        }
        findOneByPersonAndJob(staff.getPerson(), staff.getJob()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, staff)) {
                throw new BusinessException("staff.save.unique", staff.getPerson().getName(),
                        staff.getJob().getName());
            }
        });
        staff.setDepartment(staff.getJob().getDepartment());
        staff.setOrganization(staff.getJob().getDepartment().getOrganization());
        super.save(staff);
        if (staff.isPrimary()) {
            setPrimaryJob(staff.getPerson(), staff.getJob());
        }
        return staff;
    }

    @Override
    public void setPrimaryJob(final Person person, final Job job) {
        findAllByPerson(person).forEach(staff -> {
            if (EntityUtils.equals(staff.getJob(), job)) {
                staff.setPrimary(true);
                super.save(staff);
            } else {
                if (staff.isPrimary()) {
                    staff.setPrimary(false);
                    super.save(staff);
                }
            }
        });
    }

    @Override
    public Optional<Staff> findOneByPersonAndPrimaryTrue(final Person person) {
        return getRepository().findOneByPersonIdAndPrimaryTrue(person.getId());
    }

    @Override
    public Optional<Staff> findOneByPersonAndJob(Person person, Job job) {
        return getRepository().findOneByPersonIdAndJobId(person.getId(), job.getId());
    }

    @Override
    public List<Staff> findAllByPerson(final Person person) {
        return getRepository().findAllByPersonId(person.getId());
    }

    @Override
    public List<Staff> findAllByJobIn(final List<Job> jobs) {
        return getRepository().findAllByJobIdIn(jobs.stream().map(Job::getId).toList());
    }

    @Override
    public List<Staff> findAllByDepartmentAndRoleCodes(@NotNull Department department, @NotEmpty String... roleCodes) {
        final var root = QStaff.staff;
        final var jobRoles = root.job.jobRoles;
        final var jobRole = QJobRole.jobRole;
        return findAll(root.department.id.eq(department.getId())
                .and(JPAExpressions.selectOne().from(jobRoles, jobRole).where(jobRole.role.code.in(roleCodes))
                        .exists()),
                null);
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Order.asc("job.parentIds"), Order.asc("job.sequence"));
    }

}
