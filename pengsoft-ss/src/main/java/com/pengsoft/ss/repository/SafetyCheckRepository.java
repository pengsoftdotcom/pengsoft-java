package com.pengsoft.ss.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.QSafetyCheck;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyCheck} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyCheckRepository extends EntityRepository<QSafetyCheck, SafetyCheck, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QSafetyCheck root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link SafetyCheck} with the given code.
     *
     * @param code {@link SafetyCheck}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<SafetyCheck> findOneByCode(@NotBlank String code);

    /**
     * 查询指定时间段内的工程项目的安全检查天数
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select project, count(1) count from (
                select project_id project, extract(DAY from submitted_at) as day, count(1) count
                  from safety_check where project_id in (:projectIds) and submitted_at between :startTime and :endTime
                  group by project_id, day
              ) a group by project
                    """, nativeQuery = true)
    List<Map<String, Object>> getCheckedDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询指定时间段内的工程项目的安全检查统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select
              project_id project,
              b.code status,
              case when a.handled_at is null then false else true end handled,
              count(1) count
            from safety_check a
              left join dictionary_item b on a.status_id = b.id
            where a.project_id in (:projectIds) and a.submitted_at between :startTime and :endTime
            group by project_id, status, handled
                      """, nativeQuery = true)
    List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询检查人指定时间段内的工程项目的安全检查统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param checkerIds 检查人ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select project, checker, count(1) from (
              select
                project_id project,
                checker_id checker,
                extract(DAY from submitted_at) as day,
                count(1) count
              from safety_check where project_id in (:projectIds) and checker_id in (:checkerIds) and submitted_at between :startTime and :endTime
              group by project, checker, day
            ) a group by project, checker
                          """, nativeQuery = true)
    List<Map<String, Object>> statisticByChecker(@NotEmpty List<String> projectIds, @NotEmpty List<String> checkerIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime);
}
