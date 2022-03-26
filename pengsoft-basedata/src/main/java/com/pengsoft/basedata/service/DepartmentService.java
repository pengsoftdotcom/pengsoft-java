package com.pengsoft.basedata.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.support.service.TreeEntityService;

/**
 * The service interface of {@link Department}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface DepartmentService extends TreeEntityService<Department, String> {

    /**
     * Returns an {@link Optional} of a {@link Department} with the given
     * organization,
     * parent and name.
     *
     * @param organization The {@link Department}'s organization
     * @param parent       The {@link Department}'s parent
     * @param name         The {@link Department}'s name
     */
    Optional<Department> findOneByOrganizationAndParentAndName(@NotNull Organization organization, Department parent,
            @NotBlank String name);

}
