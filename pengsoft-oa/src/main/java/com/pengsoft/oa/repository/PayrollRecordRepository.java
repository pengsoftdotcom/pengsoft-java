package com.pengsoft.oa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.QPayrollRecord;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link PayrollRecord,} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PayrollRecordRepository extends EntityRepository<QPayrollRecord, PayrollRecord, String>, OwnedExtRepository {

    /**
     * Returns an {@link Optional} of a {@link PayrollRecord} with the given year,
     * month and belongsTo.
     * 
     * @param year      The payroll record year
     * @param month     The payroll record month
     * @param belongsTo The coding rule's belongsTo
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<PayrollRecord> findOneByYearAndMonthAndBelongsTo(int year, int month, String belongsTo);

    /**
     * 统计指定时间段的工资统计数据
     * 
     * @param organizationIds 机构ID列表
     * @param startTime       开始时间
     * @param endTime         结束时间
     */
    @Query(value = """
            select
                a.belongs_to organization,
                b.code status,
                count(1) count
            from payroll_record a
                left join dictionary_item b on a.status_id = b.id
            where a.belongs_to in (?1) and a.created_at between ?2 and ?3
            group by organization, status
            """, nativeQuery = true)
    List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

}
