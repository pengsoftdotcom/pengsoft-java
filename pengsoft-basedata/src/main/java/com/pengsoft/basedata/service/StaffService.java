package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Staff}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StaffService extends EntityService<Staff, String> {

    /**
     * Set the primary job.
     *
     * @param person The {@link Person}.
     * @param job    The primary {@link Job}.
     */
    void setPrimaryJob(@NotNull Person person, @NotNull Job job);

    /**
     * Returns an {@link Optional} of a {@link Staff} with the given person and
     * primary
     * true.
     *
     * @param person The person
     */
    Optional<Staff> findOneByPersonAndPrimaryTrue(@NotNull Person person);

    /**
     * Returns an {@link Optional} of a {@link Staff} with the given user id and
     * primary true.
     *
     * @param user The user.
     */
    Optional<Staff> findOneByPersonUserAndPrimaryTrue(@NotNull User user);

    /**
     * Returns an {@link Optional} of a {@link Staff} with the given person and job.
     *
     * @param person The {@link Staff}'s person
     * @param job    The {@link Staff}'s job
     */
    Optional<Staff> findOneByPersonAndJob(@NotNull Person person, @NotNull Job job);

    /**
     * Returns all {@link Staff}s with the given person.
     *
     * @param person {@link Staff}'s person
     */
    List<Staff> findAllByPerson(@NotNull Person person);

    /**
     * Returns all {@link Staff}s with the given jobs
     *
     * @param jobs The {@link Staff}'s job
     */
    List<Staff> findAllByJobIn(@NotEmpty List<Job> jobs);

    /**
     * 根据指定的部门和角色查询员工
     * 
     * @param departments 部门
     * @param roleCodes   角色编码
     */
    List<Staff> findAllByDepartmentsAndRoleCodes(@NotEmpty List<Department> departments,
            @NotEmpty List<String> roleCodes);

    /**
     * 根据指定的部门、人员和角色查询员工
     * 
     * @param departments 部门
     * @param persons     人员
     * @param roleCodes   角色编码
     */
    List<Staff> findAllByDepartmentsAndRoleCodes(@NotEmpty List<Department> departments, @NotEmpty List<Person> persons,
            @NotEmpty List<String> roleCodes);

}
