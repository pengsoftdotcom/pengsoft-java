package com.pengsoft.ss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

/**
 * The service interface of {@link SafetyTraining}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingService extends EntityService<SafetyTraining, String> {

    /**
     * 提交
     * 
     * @param training {@link SafetyTraining}
     */
    void saveAndSubmit(@Valid @NotNull SafetyTraining training);

    /**
     * 提交
     * 
     * @param training {@link SafetyTraining}
     */
    void submit(@NotNull SafetyTraining training);

    /**
     * 开始
     * 
     * @param training {@link SafetyTraining}
     */
    void start(@NotNull SafetyTraining training);

    /**
     * 结束，并上传过程拍照
     * 
     * @param training {@link SafetyTraining}
     * @param files    培训过程照片
     */
    void end(@NotNull SafetyTraining training, @NotEmpty List<Asset> files);

    /**
     * Returns an {@link Optional} of a {@link SafetyTraining}
     * with the given
     * code.
     *
     * @param code {@link SafetyTraining}'s code
     */
    Optional<SafetyTraining> findOneByCode(@NotBlank String code);

    /**
     * 查询指定时间段内的工程项目的安全培训天数
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> getTrainedDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询指定时间段内的工程项目的安全培训统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询培训人指定时间段内的工程项目的安全培训统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param trainerIds 培训人ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> statisticByTrainer(@NotEmpty List<String> projectIds, @NotEmpty List<String> trainerIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime);

}
