package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.QJob;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

/**
 * The repository interface of {@link Job} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface JobRepository extends TreeEntityRepository<QJob, Job, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QJob root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Job} with the given department id,
     * parent id and name.
     *
     * @param departmentId The id of {@link Job}'s department
     * @param parentId     The id of {@link Job}'s parent
     * @param name         The {@link Job}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Job> findOneByDepartmentIdAndParentIdAndName(@NotBlank String departmentId, String parentId,
            @NotBlank String name);

    /**
     * 根据名称查询所有职位
     * 
     * @param name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Job> findAllByName(@NotBlank String name);

}
