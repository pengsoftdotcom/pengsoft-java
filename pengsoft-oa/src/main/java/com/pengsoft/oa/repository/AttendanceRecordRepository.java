package com.pengsoft.oa.repository;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.oa.domain.QAttendanceRecord;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link AttendanceRecord} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface AttendanceRecordRepository
        extends EntityRepository<QAttendanceRecord, AttendanceRecord, String>, OwnedExtRepository {

}
