package com.pengsoft.oa.service;

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

}
