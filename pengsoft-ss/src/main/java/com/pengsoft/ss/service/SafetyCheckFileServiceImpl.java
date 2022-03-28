package com.pengsoft.ss.service;

import javax.inject.Inject;

import com.pengsoft.ss.domain.SafetyCheckFile;
import com.pengsoft.ss.repository.SafetyCheckFileRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of
 * {@link SafetyCheckFileService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SafetyCheckFileServiceImpl extends EntityServiceImpl<SafetyCheckFileRepository, SafetyCheckFile, String>
        implements SafetyCheckFileService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(SafetyCheckFile entity) {
        super.delete(entity);
        assetService.delete(entity.getFile());
    }

}
