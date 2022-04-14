package com.pengsoft.ss.service;

import javax.inject.Inject;

import com.pengsoft.ss.domain.QualityCheckFile;
import com.pengsoft.ss.repository.QualityCheckFileRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link QualityCheckFileService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class QualityCheckFileServiceImpl extends EntityServiceImpl<QualityCheckFileRepository, QualityCheckFile, String>
        implements QualityCheckFileService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(QualityCheckFile entity) {
        super.delete(entity);
        assetService.delete(entity.getFile());
    }

}
