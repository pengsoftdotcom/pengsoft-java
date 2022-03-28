package com.pengsoft.ss.service;

import javax.inject.Inject;

import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.ss.repository.SafetyTrainingFileRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of
 * {@link SafetyTrainingFileService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SafetyTrainingFileServiceImpl
        extends EntityServiceImpl<SafetyTrainingFileRepository, SafetyTrainingFile, String>
        implements SafetyTrainingFileService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(SafetyTrainingFile entity) {
        super.delete(entity);
        assetService.delete(entity.getFile());
    }

}
