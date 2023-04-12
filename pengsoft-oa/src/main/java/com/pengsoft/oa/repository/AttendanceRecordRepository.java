package com.pengsoft.oa.repository;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.repository.Query;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.AttendanceRecord;
import com.pengsoft.oa.domain.QAttendanceRecord;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link AttendanceRecord} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AttendanceRecordRepository
        extends EntityRepository<QAttendanceRecord, AttendanceRecord, String>, OwnedExtRepository {

    /**
     * 指定时间的考勤统计数据
     * 
     * @param organizationIds 机构ID列表
     * @param departmentIds   部门ID列表
     * @param year            年份
     * @param month           月份
     * @param day             日期
     */
    @Query(value = """
            select
              b.organization_id organization,
              b.department_id department,
              count(1) count,
              count(
                case
                  when a.enter_record_id is not null then 1
                  else 0
                end
              ) entered,
              count(
                case
                  when a.exit_record_id is not null then 1
                  else 0
                end
              ) exited,
              count(
                case
                  when a.illegal = true then 1
                  else 0
                end
              ) illegal
            from attendance_record a
              left join staff b on a.staff_id = b.id
            where b.organization_id in (?1)
              and b.department_id in (?2)
              and a.year = ?3
              and (?4 = 0 or a.month = ?4)
              and (?5 = 0 or a.day = ?5)
            group by b.organization_id, b.department_id
                  """, nativeQuery = true)
    List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds, @NotEmpty List<String> departmentIds,
            int year, int month, int day);

}
