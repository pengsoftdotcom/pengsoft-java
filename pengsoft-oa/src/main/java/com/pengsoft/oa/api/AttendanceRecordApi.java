package com.pengsoft.oa.api;

import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.oa.service.AttendanceRecordService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
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

}