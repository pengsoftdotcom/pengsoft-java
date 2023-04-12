package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

/**
 * The repository interface of {@link Staff} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StaffRepository extends EntityRepository<QStaff, Staff, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QStaff root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.person.name).first(StringPath::contains);
        bindings.bind(root.person.nickname).first(StringPath::contains);
        bindings.bind(root.person.mobile).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Staff} with the given person and job.
     *
     * @param personId The id of {@link Staff}'s person
     * @param jobId    The id of {@link Staff}'s job
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Staff> findOneByPersonIdAndJobId(@NotBlank String personId, @NotBlank String jobId);

    /**
     * Returns an {@link Optional} of a {@link Staff} with the given person id and
     * primary true.
     *
     * @param personId The person id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Staff> findOneByPersonIdAndPrimaryTrue(@NotBlank String personId);

    /**
     * Returns an {@link Optional} of a {@link Staff} with the given user id and
     * primary true.
     *
     * @param userId The user id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Staff> findOneByPersonUserIdAndPrimaryTrue(@NotBlank String userId);

    /**
     * Returns all {@link Staff}s with the given person.
     *
     * @param personId The id of {@link Staff}'s person
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Staff> findAllByPersonId(@NotNull String personId);

    /**
     * Returns a {@link Pageable} of {@link Staff}s with the given jobs
     *
     * @param jobIds   A collection of {@link Job}'s id
     * @param pageable {@link Pageable}
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Page<Staff> findPageByJobIdIn(@NotEmpty List<String> jobIds, Pageable pageable);

    /**
     * Returns all {@link Staff}s with the given jobs
     *
     * @param jobIds A collection of {@link Job}'s id
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Staff> findAllByJobIdIn(@NotEmpty List<String> jobIds);

    /**
     * Returns all {@link Staff}s with the given organization
     * 
     * @param organizationId
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Staff> findAllByJobDepartmentOrganizationId(@NotBlank String organizationId);

    /**
     * Returns the total count with the given job.
     * 
     * @param jobId The id of {@link Staff}'s job
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    long countByJobId(@NotBlank String jobId);

}
