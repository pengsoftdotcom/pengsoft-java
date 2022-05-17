package com.pengsoft.oa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link PayrollRecord}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PayrollRecordService extends EntityService<PayrollRecord, String> {

    /**
     * Returns an {@link Optional} of a {@link PayrollRecord} with the given year,
     * month and organization.
     * 
     * @param year      The payroll record year
     * @param month     The payroll record month
     * @param belongsTo The payroll record belongsTo
     */
    Optional<PayrollRecord> findOneByYearAndMonthAndBelongsTo(int year, int month, @NotBlank String belongsTo);

    /**
     * 统计指定时间段的工资统计数据
     * 
     * @param organizationIds 机构ID列表
     * @param startTime       开始时间
     * @param endTime         结束时间
     */
    List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

}