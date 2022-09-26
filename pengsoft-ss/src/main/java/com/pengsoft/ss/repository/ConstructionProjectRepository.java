package com.pengsoft.ss.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QConstructionProject;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

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
     * 根据监控单位负责人ID查询所有工程项目列表
     * 
     * @param ruManagerDepartmentId 监控单位负责人部门ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<ConstructionProject> findAllByRuManagerDepartmentId(@NotBlank String ruManagerDepartmentId);

    /**
     * 根据建设单位负责人ID查询所有工程项目列表
     * 
     * @param ownerManagerDepartmentId 建设单位负责人部门ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<ConstructionProject> findAllByOwnerManagerDepartmentId(@NotBlank String ownerManagerDepartmentId);

    /**
     * 根据监理单位负责人ID查询所有工程项目列表
     * 
     * @param suManagerDepartmentId 监理单位负责人部门ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<ConstructionProject> findAllBySuManagerDepartmentId(@NotBlank String suManagerDepartmentId);

    /**
     * 根据施工单位负责人ID查询所有工程项目列表
     * 
     * @param buManagerDepartmentId 施工单位负责人部门ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<ConstructionProject> findAllByBuManagerDepartmentId(@NotBlank String buManagerDepartmentId);

    /**
     * 按状态统计
     * 
     * @param departmentId 项目部ID
     */
    @Query(value = """
            select
              b.code,
              a.count
            from (
              select status_id, count(1)
              from construction_project a
                left join staff b on a.ru_manager_id = b.id
                left join staff c on a.owner_manager_id = c.id
                left join staff d on a.su_manager_id = d.id
                left join staff e on a.bu_manager_id = e.id
              where
                b.department_id = ?1
                or c.department_id = ?1
                or d.department_id = ?1
                or e.department_id = ?1
              group by status_id
            ) a left join dictionary_item b on a.status_id = b.id
                  """, nativeQuery = true)
    List<Map<String, Object>> statisticByStatus(String departmentId);
}
