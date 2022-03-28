package com.pengsoft.ss.facade;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.ss.domain.QSafetyTrainingFile;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.service.SafetyTrainingFileService;
import com.pengsoft.ss.service.SafetyTrainingService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;

import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SafetyTrainingFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class SafetyTrainingFacadeImpl extends EntityFacadeImpl<SafetyTrainingService, SafetyTraining, String>
        implements SafetyTrainingFacade {

    @Inject
    private AssetService assetService;

    @Inject
    private SafetyTrainingFileService safetyTraininigFileService;

    @Override
    public Optional<SafetyTraining> findOneByCode(String code) {
        return getService().findOneByCode(code);
    }

    @Override
    public void deleteFileByAsset(SafetyTraining check, Asset asset) {
        if (check == null) {
            assetService.delete(asset);
        } else {
            safetyTraininigFileService.findOne(QSafetyTrainingFile.safetyTrainingFile.file.id.eq(asset.getId()))
                    .ifPresent(safetyTraininigFileService::delete);
        }
    }

    @Override
    public void saveAndSubmit(SafetyTraining training) {
        getService().saveAndSubmit(training);
    }

    @Override
    public void submit(SafetyTraining training) {
        getService().submit(training);

    }

    @Override
    public void start(SafetyTraining training) {
        getService().start(training);
    }

    @Override
    public void end(SafetyTraining training, List<Asset> files) {
        getService().end(training, files);
    }

    @Override
    public void delete(SafetyTraining entity) {
        safetyTraininigFileService.delete(entity.getFiles());
        super.delete(entity);
    }

}
