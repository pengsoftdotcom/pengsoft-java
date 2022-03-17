package com.pengsoft.basedata.service;

import java.util.ArrayList;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.PersonDetails;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.domain.StaffDetails;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.util.EntityUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link StaffDetailsService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class StaffDetailsServiceImpl extends PersonDetailsServiceImpl implements StaffDetailsService {

    @Inject
    private StaffService staffService;

    @Override
    public DefaultUserDetails setPrimaryJob(final Job job) {
        final var userDetails = (StaffDetails) SecurityUtils.getUserDetails();
        if (userDetails.getJobs().stream().anyMatch(j -> EntityUtils.equals(j, job))) {
            staffService.setPrimaryJob(SecurityUtilsExt.getPerson(), job);
            userDetails.setPrimaryJob(job);
            userDetails.setPrimaryDepartment(job.getDepartment());
            userDetails.setPrimaryOrganization(job.getDepartment().getOrganization());
            userDetails.setRoles(job.getJobRoles().stream().map(JobRole::getRole).toList());
            final var authorities = new ArrayList<GrantedAuthority>();
            userDetails.getRoles().forEach(role -> authorities.addAll(getAllAuthorities(role)));
            userDetails.setAuthorities(authorities.stream().distinct().toList());
        }
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var userDetails = (DefaultUserDetails) super.loadUserByUsername(username);
        if (userDetails instanceof PersonDetails personDetails) {
            final var person = personDetails.getPerson();
            final var roles = userDetails.getRoles();
            final var authorities = userDetails.getAuthorities();
            return staffService.findOneByPersonAndPrimaryTrue(person).map(staff -> {
                final var staffs = staffService.findAllByPerson(person);
                final var jobs = staffs.stream().map(Staff::getJob).toList();
                final var job = staff.getJob();
                roles.addAll(job.getJobRoles().stream().map(JobRole::getRole).toList());
                roles.forEach(role -> {
                    final var allAuthorities = getAllAuthorities(role);
                    if (CollectionUtils.isNotEmpty(allAuthorities)) {
                        authorities.addAll(allAuthorities);
                    }
                });
                return new StaffDetails(staff, jobs, roles, authorities);
            }).map(DefaultUserDetails.class::cast).orElse(userDetails);
        } else {
            return userDetails;
        }
    }

}
