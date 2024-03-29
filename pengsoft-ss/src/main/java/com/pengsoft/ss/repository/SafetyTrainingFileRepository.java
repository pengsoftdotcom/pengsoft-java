package com.pengsoft.ss.repository;

import com.pengsoft.ss.domain.QSafetyTrainingFile;
import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link SafetyTrainingFile} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingFileRepository
        extends EntityRepository<QSafetyTrainingFile, SafetyTrainingFile, String> {

}
