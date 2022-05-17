package com.pengsoft.ss.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.ss.domain.QSafetyTrainingConfirmFile;
import com.pengsoft.ss.domain.QSafetyTrainingFile;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.service.SafetyTrainingConfirmFileService;
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

    @Inject
    private SafetyTrainingConfirmFileService safetyTraininigConfirmFileService;

    @Override
    public Optional<SafetyTraining> findOneByCode(String code) {
        return getService().findOneByCode(code);
    }

    @Override
    public void deleteFileByAsset(SafetyTraining training, Asset asset) {
        if (training == null) {
            assetService.delete(asset);
        } else {
            safetyTraininigFileService.findOne(QSafetyTrainingFile.safetyTrainingFile.file.id.eq(asset.getId()))
                    .ifPresent(safetyTraininigFileService::delete);
        }
    }

    @Override
    public void deleteConfirmFileByAsset(SafetyTraining training, Asset asset) {
        if (training == null) {
            assetService.delete(asset);
        } else {
            safetyTraininigConfirmFileService
                    .findOne(QSafetyTrainingConfirmFile.safetyTrainingConfirmFile.file.id.eq(asset.getId()))
                    .ifPresent(safetyTraininigConfirmFileService::delete);
        }
    }

    @Override
    public void submit(@Valid @NotNull SafetyTraining training, @NotEmpty List<Staff> staffs) {
        getService().submit(training, staffs);
    }

    @Override
    public void start(SafetyTraining training) {
        getService().start(training);
    }

    @Override
    public void end(SafetyTraining training, List<Asset> files, List<Asset> confirmFiles) {
        getService().end(training, files, confirmFiles);
    }

    @Override
    public void delete(SafetyTraining training) {
        safetyTraininigFileService.delete(training.getFiles());
        safetyTraininigConfirmFileService.delete(training.getConfirmFiles());
        super.delete(training);
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
