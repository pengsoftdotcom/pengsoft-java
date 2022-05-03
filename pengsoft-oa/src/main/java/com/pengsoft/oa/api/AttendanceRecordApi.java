package com.pengsoft.oa.api;

import java.util.List;
import java.util.Map;

import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.oa.service.AttendanceRecordService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link AttendanceRecord}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/oa/attendance-record")
public class AttendanceRecordApi extends EntityApi<AttendanceRecordService, AttendanceRecord, String> {

    @GetMapping("statistic")
    public List<Map<String, Object>> statistic(
            @RequestParam(value = "organization.id", required = false) List<String> organizationIds,
            @RequestParam(value = "department.id", required = false) List<String> departmentIds,
            int year, int month, int day) {
        return getService().statistic(organizationIds, departmentIds, year, month, day);
    }

}