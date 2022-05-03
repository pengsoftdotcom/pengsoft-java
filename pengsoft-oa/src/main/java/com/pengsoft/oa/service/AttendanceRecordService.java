package com.pengsoft.oa.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link AttendanceRecord}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AttendanceRecordService extends EntityService<AttendanceRecord, String> {

    /**
     * 指定时间的考勤统计数据
     * 
     * @param organizationIds 机构ID列表
     * @param departmentIds   部门ID列表
     * @param year            年份
     * @param month           月份
     * @param day             日期
     */
    List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds, @NotEmpty List<String> departmentIds,
            int year, int month, int day);

}
