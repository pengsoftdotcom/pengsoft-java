package com.pengsoft.ss.repository;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyTrainingParticipant} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyTrainingParticipantRepository
        extends EntityRepository<QSafetyTrainingParticipant, SafetyTrainingParticipant, String> {

    /**
     * 安全培训是否存在参与人
     * 
     * @param trainingId 安全培训ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    boolean existsByTrainingId(@NotBlank String trainingId);

}
