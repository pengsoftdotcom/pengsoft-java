package com.pengsoft.ss.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link SafetyTrainingParticipant} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingParticipantRepository extends EntityRepository<QSafetyTrainingParticipant, SafetyTrainingParticipant, String> {

    /**
     * 安全培训是否存在参与人
     * 
     * @param trainingId 安全培训ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    boolean existsByTrainingId(@NotBlank String trainingId);

    /**
     * 返回该培训的所有参与人
     * 
     * @param trainingId 安全培训ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<SafetyTrainingParticipant> findAllByTrainingId(@NotBlank String trainingId);

    /**
     * 按工程项目、参与状态统计
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    @Query(value = """
            select project_id project, d.code status, count
            from (
              select b.project_id, a.status_id, count(1) count
              from safety_training_participant a
                left join safety_training b on a.training_id = b.id
              where b.project_id in (:projectIds) and b.submitted_at between :startTime and :endTime
              group by b.project_id, a.status_id
            ) c left join dictionary_item d on c.status_id = d.id
                """, nativeQuery = true)
    List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

}
