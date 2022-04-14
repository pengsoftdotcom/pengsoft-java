package com.pengsoft.ss.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.QQualityCheckFile;
import com.pengsoft.ss.domain.QualityCheck;
import com.pengsoft.ss.service.QualityCheckFileService;
import com.pengsoft.ss.service.QualityCheckService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;

import org.springframework.stereotype.Service;

/**
 * The implementer of {@link QualityCheckFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class QualityCheckFacadeImpl extends EntityFacadeImpl<QualityCheckService, QualityCheck, String>
        implements QualityCheckFacade {

    @Inject
    private AssetService assetService;

    @Inject
    private QualityCheckFileService qualityCheckFileService;

    @Override
    public void submit(QualityCheck check, List<Asset> assets) {
        getService().submit(check, assets);
    }

    @Override
    public void handle(QualityCheck check, String result, List<Asset> assets) {
        getService().handle(check, result, assets);
    }

    @Override
    public Optional<QualityCheck> findOneByCode(String code) {
        return getService().findOneByCode(code);
    }

    @Override
    public void deleteFileByAsset(QualityCheck check, Asset asset) {
        if (check == null) {
            assetService.delete(asset);
        } else {
            qualityCheckFileService.findOne(QQualityCheckFile.qualityCheckFile.file.id.eq(asset.getId()))
                    .ifPresent(qualityCheckFileService::delete);
        }
    }

    @Override
    public void delete(QualityCheck entity) {
        qualityCheckFileService.delete(entity.getFiles());
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

}
