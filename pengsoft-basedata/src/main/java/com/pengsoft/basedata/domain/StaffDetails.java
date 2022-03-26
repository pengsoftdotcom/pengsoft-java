package com.pengsoft.basedata.domain;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.basedata.json.DepartmentJsonSerializer;
import com.pengsoft.basedata.json.JobCollectionJsonSerializer;
import com.pengsoft.basedata.json.JobJsonSerializer;
import com.pengsoft.basedata.json.OrganizationJsonSerializer;
import com.pengsoft.security.domain.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * The implementer of {@link UserDetails} for {@link Staff}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class StaffDetails extends PersonDetails {

    private static final long serialVersionUID = -3146919588102557429L;

    @JsonSerialize(using = JobCollectionJsonSerializer.class)
    private Collection<Job> jobs;

    @JsonIgnore
    private Staff staff;

    @JsonSerialize(using = JobJsonSerializer.class)
    private Job primaryJob;

    @JsonSerialize(using = OrganizationJsonSerializer.class)
    private Organization primaryOrganization;

    @JsonSerialize(using = DepartmentJsonSerializer.class)
    private Department primaryDepartment;

    public StaffDetails(final Staff staff, final List<Job> jobs, final List<Role> roles,
            final List<GrantedAuthority> authorities) {
        super(staff.getPerson(), roles, null, authorities);
        setJobs(jobs);
        setStaff(staff);
        setPrimaryJob(staff.getJob());
        setPrimaryDepartment(staff.getDepartment());
        setPrimaryOrganization(staff.getDepartment().getOrganization());
    }

}
