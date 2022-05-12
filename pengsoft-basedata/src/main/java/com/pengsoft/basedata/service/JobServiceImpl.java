package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.repository.JobRepository;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.security.domain.Role;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link JobService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class JobServiceImpl extends TreeEntityServiceImpl<JobRepository, Job, String> implements JobService {

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Override
    public Job save(final Job job) {
        final var department = job.getDepartment();
        final var parent = Optional.ofNullable(job.getParent()).orElse(null);
        findOneByDepartmentAndParentAndName(department, parent, job.getName()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, job)) {
                throw getExceptions().constraintViolated("name", "exists", job.getName());
            }
        });
        return super.save(job);
    }

    @Override
    public void grantRoles(final Job job, final List<Role> roles) {
        final var source = job.getJobRoles();
        final var target = roles.stream().map(role -> new JobRole(job, role)).toList();
        final var deleted = source.stream().filter(s -> target.stream().noneMatch(
                t -> EntityUtils.equals(s.getJob(), t.getJob()) && EntityUtils.equals(s.getRole(), t.getRole())))
                .toList();
        jobRoleRepository.deleteAll(deleted);
        source.removeAll(deleted);
        final var created = target.stream().filter(t -> source.stream().noneMatch(
                s -> EntityUtils.equals(t.getJob(), s.getJob()) && EntityUtils.equals(t.getRole(), s.getRole())))
                .toList();
        jobRoleRepository.saveAll(created);
        source.addAll(created);
        super.save(job);
    }

    @Override
    public Optional<Job> findOneByDepartmentAndParentAndName(Department department, Job parent, String name) {
        return getRepository().findOneByDepartmentIdAndParentIdAndName(department.getId(),
                Optional.ofNullable(parent).map(Job::getId).orElse(null), name);
    }

    @Override
    public List<Job> findAllByName(@NotBlank String name) {
        return getRepository().findAllByName(name);
    }

}
