package com.pengsoft.ss.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.ss.domain.QQualityCheckFile;
import com.pengsoft.ss.domain.QualityCheckFile;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link QualityCheckFile} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface QualityCheckFileRepository extends EntityRepository<QQualityCheckFile, QualityCheckFile, String> {

    /**
     * Returns all quality check files with the given quality check id and quality
     * check file type code.
     * 
     * @param checkId  The quality check id.
     * @param typeCode The quality check file type code.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<QualityCheckFile> findAllByCheckIdAndTypeCode(@NotBlank String checkId, @NotBlank String typeCode);

}
