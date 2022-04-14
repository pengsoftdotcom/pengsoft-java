package com.pengsoft.ss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.QualityCheck;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

/**
 * The service interface of {@link QualityCheck}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface QualityCheckService extends EntityService<QualityCheck, String> {

    /**
     * 提交
     * 
     * @param check  {@link QualityCheck}
     * @param assets A collection of {@link Asset}
     */
    void submit(@Valid @NotNull QualityCheck check, @NotEmpty List<Asset> assets);

    /**
     * 处理
     * 
     * @param check  {@link QualityCheck}
     * @param result 处理结果
     * @param assets A collection of {@link Asset}
     */
    void handle(@NotNull QualityCheck check, @NotBlank String result, @NotEmpty List<Asset> assets);

    /**
     * Returns an {@link Optional} of a {@link QualityCheck}
     * with the given
     * code.
     *
     * @param code {@link QualityCheck}'s code
     */
    Optional<QualityCheck> findOneByCode(@NotBlank String code);

    /**
     * 查询指定时间段内的工程项目的安全检查天数
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> getCheckedDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询指定时间段内的工程项目的安全检查统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

    /**
     * 查询检查人指定时间段内的工程项目的安全检查统计数据
     * 
     * @param projectIds 工程项目ID列表
     * @param checkerIds 检查人ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> statisticByChecker(@NotEmpty List<String> projectIds, @NotEmpty List<String> checkerIds,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime);

}
