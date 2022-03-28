package com.pengsoft.ss.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.ss.domain.QSafetyCheckFile;
import com.pengsoft.ss.domain.SafetyCheckFile;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SafetyCheckFile}
 * based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SafetyCheckFileRepository
        extends OwnedExtRepository, EntityRepository<QSafetyCheckFile, SafetyCheckFile, String> {

    /**
     * Returns all safety check files with the given safety check id and safety
     * check file type code.
     * 
     * @param checkId  The safety check id.
     * @param typeCode The safety check file type code.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<SafetyCheckFile> findAllByCheckIdAndTypeCode(@NotBlank String checkId, @NotBlank String typeCode);

}
