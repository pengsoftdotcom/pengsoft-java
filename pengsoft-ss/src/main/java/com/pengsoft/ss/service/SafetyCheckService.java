package com.pengsoft.ss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The service interface of {@link SafetyCheck}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyCheckService extends EntityService<SafetyCheck, String> {

    /**
     * 提交
     * 
     * @param check  {@link SafetyCheck}
     * @param assets A collection of {@link Asset}
     */
    void submit(@Valid @NotNull SafetyCheck check, @NotEmpty List<Asset> assets);

    /**
     * 处理
     * 
     * @param check  {@link SafetyCheck}
     * @param result 处理结果
     * @param assets A collection of {@link Asset}
     */
    void handle(@NotNull SafetyCheck check, @NotBlank String result, @NotEmpty List<Asset> assets);

    /**
     * Returns an {@link Optional} of a {@link SafetyCheck}
     * with the given
     * code.
     *
     * @param code {@link SafetyCheck}'s code
     */
    Optional<SafetyCheck> findOneByCode(@NotBlank String code);

    /**
     * 返回指定类型编码、状态编码且提交时间在指定时间段内的安全检查数
     * 
     * @param typeCode   类型编码
     * @param statusCode 状态编码
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    long countByTypeCodeAndStatusCodeAndSubmittedAtBetween(@NotBlank String typeCode, @NotBlank String statusCode,
            @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime);

    /**
     * 返回指定类型编码、状态编码且提交时间在指定时间段内的安全检查分页数据
     * 
     * @param typeCode   类型编码
     * @param statusCode 状态编码
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageable   分页参数
     */
    Page<SafetyCheck> findPageByTypeCodeAndStatusCodeAndSubmittedAtBetween(@NotBlank String typeCode,
            @NotBlank String statusCode, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime,
            Pageable pageable);

    /**
     * 查询指定时间段内的工程项目的安全检查天数
     * 
     * @param projectIds 工程项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> getDays(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
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

}
