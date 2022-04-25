package com.pengsoft.ss.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.QSafetyCheckFile;
import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.service.SafetyCheckFileService;
import com.pengsoft.ss.service.SafetyCheckService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;

import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SafetyCheckFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class SafetyCheckFacadeImpl extends EntityFacadeImpl<SafetyCheckService, SafetyCheck, String>
        implements SafetyCheckFacade {

    @Inject
    private AssetService assetService;

    @Inject
    private SafetyCheckFileService safetyCheckFileService;

    @Override
    public void submit(SafetyCheck check, List<Asset> assets) {
        getService().submit(check, assets);
    }

    @Override
    public void handle(SafetyCheck check, String result, List<Asset> assets) {
        getService().handle(check, result, assets);
    }

    @Override
    public Optional<SafetyCheck> findOneByCode(String code) {
        return getService().findOneByCode(code);
    }

    @Override
    public void deleteFileByAsset(SafetyCheck check, Asset asset) {
        if (check == null) {
            assetService.delete(asset);
        } else {
            safetyCheckFileService.findOne(QSafetyCheckFile.safetyCheckFile.file.id.eq(asset.getId()))
                    .ifPresent(safetyCheckFileService::delete);
        }
    }

    @Override
    public void delete(SafetyCheck entity) {
        safetyCheckFileService.delete(entity.getFiles());
        super.delete(entity);
    }

    @Override
    public List<Map<String, Object>> getCheckedDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().getCheckedDays(projectIds, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().statistic(projectIds, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statisticByChecker(@NotEmpty List<String> projectIds,
            @NotEmpty List<String> checkerIds, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().statisticByChecker(projectIds, checkerIds, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> findAllUncheckedOrUnhandledDates(@NotEmpty List<String> projectIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return getService().findAllUncheckedOrUnhandledDates(projectIds, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statisticByDay(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().statisticByDay(projectIds, startTime, endTime);
    }

}
