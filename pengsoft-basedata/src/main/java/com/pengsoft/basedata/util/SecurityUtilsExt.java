package com.pengsoft.basedata.util;

import java.util.List;
import java.util.Optional;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.security.util.SecurityUtils;

/**
 * {@link SecurityUtils} extension
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class SecurityUtilsExt {

    private SecurityUtilsExt() {

    }

    /**
     * Returns current person.
     */
    public static Person getPerson() {
        return SecurityUtils.get("person", Person.class);
    }

    /**
     * Returns current person's id.
     */
    public static String getPersonId() {
        return Optional.ofNullable(getPerson()).map(Person::getId).orElse(null);
    }

    /**
     * Returns current staff.
     */
    public static Staff getStaff() {
        return SecurityUtils.get("staff", Staff.class);
    }

    /**
     * Returns the person's primary job.
     */
    public static Job getPrimaryJob() {
        return SecurityUtils.get("primaryJob", Job.class);
    }

    /**
     * Returns the person's primary department.
     */
    public static Department getPrimaryDepartment() {
        return Optional.ofNullable(SecurityUtils.get("primaryDepartment", Department.class)).orElse(null);
    }

    /**
     * Returns the person's primary department id.
     */
    public static String getPrimaryDepartmentId() {
        return Optional.ofNullable(getPrimaryDepartment()).map(Department::getId).orElse(null);
    }

    /**
     * Returns the person's primary organization.
     */
    public static Organization getPrimaryOrganization() {
        return Optional.ofNullable(SecurityUtils.get("primaryOrganization", Organization.class)).orElse(null);
    }

    /**
     * Returns the person's primary organization id.
     */
    public static String getPrimaryOrganizationId() {
        return Optional.ofNullable(getPrimaryOrganization()).map(Organization::getId).orElse(null);
    }

    /**
     * Returns current perons's jobs.
     */
    @SuppressWarnings("unchecked")
    public static List<Job> getJobs() {
        return Optional.ofNullable(SecurityUtils.get("jobs", List.class)).orElse(List.of());
    }

}
