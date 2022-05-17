package com.pengsoft.ss.service;

import javax.inject.Inject;

import com.pengsoft.ss.domain.SafetyTrainingConfirmFile;
import com.pengsoft.ss.repository.SafetyTrainingConfirmFileRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SafetyTrainingConfirmFileService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SafetyTrainingConfirmFileServiceImpl
        extends EntityServiceImpl<SafetyTrainingConfirmFileRepository, SafetyTrainingConfirmFile, String>
        implements SafetyTrainingConfirmFileService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(SafetyTrainingConfirmFile entity) {
        super.delete(entity);
        assetService.delete(entity.getFile());
    }

}
