package com.pengsoft.ss.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @Override
    public List<Map<String, Object>> getTrainedDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().getTrainedDays(projectIds, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().statistic(projectIds, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statisticByTrainer(@NotEmpty List<String> projectIds,
            @NotEmpty List<String> trainerIds, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().statisticByTrainer(projectIds, trainerIds, startTime, endTime);
    }

}
