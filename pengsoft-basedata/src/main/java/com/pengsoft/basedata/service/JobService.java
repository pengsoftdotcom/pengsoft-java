package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.security.domain.Role;
import com.pengsoft.support.service.TreeEntityService;

/**
 * The service interface of {@link Job}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface JobService extends TreeEntityService<Job, String> {

    /**
     * Grant roles.
     *
     * @param job   The {@link Job}.
     * @param roles The {@link Role}s to be granted.
     */
    void grantRoles(@NotNull Job job, List<Role> roles);

    /**
     * Returns an {@link Optional} of a {@link Job} with the given department id,
     * parent id and name.
     *
     * @param department The {@link Job}'s department
     * @param parent     The {@link Job}'s parent
     * @param name       The {@link Job}'s name
     */
    Optional<Job> findOneByDepartmentAndParentAndName(@NotNull Department department, Job parent,
            @NotBlank String name);

    /**
     * 根据名称查询所有职位
     * 
     * @param name
     */
    List<Job> findAllByName(@NotBlank String name);

}
