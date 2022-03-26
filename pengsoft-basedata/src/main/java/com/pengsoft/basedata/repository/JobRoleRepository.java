package com.pengsoft.basedata.repository;

import java.util.List;

import javax.persistence.QueryHint;

import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.QJobRole;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link JobRole} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface JobRoleRepository extends EntityRepository<QJobRole, JobRole, String> {

    /**
     * Returns all {@link JobRole} with the given department id and role code.
     *
     * @param departmentId The id of the {@link Organization}
     * @param roleCode     The code of the {@link Role}
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<JobRole> findAllByJobDepartmentIdAndRoleCode(String departmentId, String roleCode);

    /**
     * Returns all {@link JobRole} with the given organization id and role code.
     *
     * @param organizationId The id of the {@link Organization}
     * @param roleCode       The code of the {@link Role}
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<JobRole> findAllByJobDepartmentOrganizationIdAndRoleCode(String organizationId, String roleCode);

}
