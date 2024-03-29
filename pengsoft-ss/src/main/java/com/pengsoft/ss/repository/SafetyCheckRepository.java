package com.pengsoft.ss.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.ss.domain.QSafetyCheck;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

/**
 * The repository interface of {@link SafetyCheck} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
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

    /**
     * 查询指定时间段内的工程项目的未进行安全检查或未整改隐患的日期
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select c.name, c.date from (
                select b.id, b.name, a.date from (
                    select to_char(generate_series(cast(:startTime as timestamp), cast(:endTime as timestamp), '1 day'), 'YYYY-MM-DD') as date
                ) a cross join construction_project b
                where to_char(b.started_at, 'YYYY-MM-DD') <= a.date
                    and (b.completed_at is null or to_char(b.completed_at, 'YYYY-MM-DD') >= a.date)
                    and b.id in (:projectIds)
            ) c left join (
                select
                    project_id,
                    to_char(submitted_at, 'YYYY-MM-DD') date,
                    sum(
                        case
                            when b.code = 'risk' and a.handled_at is null then 1
                            else 0
                        end
                    ) as count
                from safety_check a
                    left join dictionary_item b on a.status_id = b.id
                where submitted_at >= :startTime and submitted_at <= :endTime
                group by project_id, date
            ) d on c.id = d.project_id and c.date = d.date
            where d.count > 0 or d.count is null
                      """, nativeQuery = true)
    List<Map<String, Object>> findAllUncheckedOrUnhandledDates(@NotEmpty List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime);

    /**
     * 按日统计指定时间段内的工程项目的安全检查数据
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select
              project_id project,
              date,
              sum(safe) safe,
              sum(risk) risk,
              sum(handled) handled,
              sum(unhandled) unhandled
            from (
              select
                project_id,
                to_char(submitted_at, 'YYYY-MM-DD') date,
                case
                  when b.code = 'safe' then 1
                  else 0
                end safe,
                case
                  when b.code = 'risk' then 1
                  else 0
                end risk,
                case
                  when b.code = 'risk' and a.handled_at is not null then 1
                  else 0
                end handled,
                case
                  when b.code = 'risk' and a.handled_at is null then 1
                  else 0
                end unhandled
              from safety_check a
                left join dictionary_item b on a.status_id = b.id
              where a.project_id in (?1) and a.submitted_at between ?2 and ?3
            ) c
            group by project, date
                  """, nativeQuery = true)
    List<Map<String, Object>> statisticByDay(@NotEmpty List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime);

}
