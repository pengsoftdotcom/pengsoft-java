package com.pengsoft.ss.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyTraining} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyTrainingRepository extends EntityRepository<QSafetyTraining, SafetyTraining, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QSafetyTraining root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.subject).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link SafetyTraining} with the given code.
     *
     * @param code {@link SafetyTraining}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<SafetyTraining> findOneByCode(@NotBlank String code);

    /**
     * 查询指定时间段内的工程项目的安全培训天数
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select project, count(1) count from (
                select project_id project, extract(DAY from submitted_at) as day, count(1) count
                  from safety_training where project_id in (:projectIds) and submitted_at between :startTime and :endTime
                  group by project_id, day
              ) a group by project
                    """, nativeQuery = true)
    List<Map<String, Object>> getTrainedDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询指定时间段内的工程项目的安全培训统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select
              project_id project,
              count(1) count
            from safety_check a
            where a.project_id in (:projectIds) and a.submitted_at between :startTime and :endTime
            group by project_id
                      """, nativeQuery = true)
    List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

}
