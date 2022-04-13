package com.pengsoft.ss.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link ConstructionProject} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface ConstructionProjectRepository
        extends EntityRepository<QConstructionProject, ConstructionProject, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QConstructionProject root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link ConstructionProject} with the given
     * code.
     *
     * @param code {@link ConstructionProject}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<ConstructionProject> findOneByCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link ConstructionProject} with the given
     * name.
     *
     * @param name {@link ConstructionProject}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<ConstructionProject> findOneByName(@NotBlank String name);

    /**
     * 按状态统计
     */
    @Query(value = """
            select
              b.code,
              a.count
            from (
              select status_id, count(1)
              from construction_project
              group by status_id
            ) a left join dictionary_item b on a.status_id = b.id
                  """, nativeQuery = true)
    List<Map<String, Object>> statisticByStatus();
}
