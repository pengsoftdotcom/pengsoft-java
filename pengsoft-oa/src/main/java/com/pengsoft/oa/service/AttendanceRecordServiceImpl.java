package com.pengsoft.oa.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.oa.repository.AttendanceRecordRepository;
import com.pengsoft.support.service.EntityServiceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link AttendanceRecord} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class AttendanceRecordServiceImpl extends EntityServiceImpl<AttendanceRecordRepository, AttendanceRecord, String>
        implements AttendanceRecordService {

    @Override
    public List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds,
            @NotEmpty List<String> departmentIds, int year, int month, int day) {
        return getRepository().statistic(organizationIds, departmentIds, year, month, day);
    }

}
