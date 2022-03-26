package com.pengsoft.ss.repository;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.ss.domain.QSafetyTrainingFile;
import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyTrainingFile}
 * based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyTrainingFileRepository
        extends OwnedExtRepository, EntityRepository<QSafetyTrainingFile, SafetyTrainingFile, String> {

}
