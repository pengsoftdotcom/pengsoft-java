package com.pengsoft.ss.repository;

import com.pengsoft.ss.domain.QSafetyTrainingConfirmFile;
import com.pengsoft.ss.domain.SafetyTrainingConfirmFile;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link SafetyTrainingConfirmFile} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingConfirmFileRepository
        extends EntityRepository<QSafetyTrainingConfirmFile, SafetyTrainingConfirmFile, String> {

}
