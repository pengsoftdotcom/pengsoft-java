package com.pengsoft.oa.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.excel.EasyExcel;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.excel.PayrollDetailData;
import com.pengsoft.oa.excel.PayrollDetailDataReadListener;
import com.pengsoft.oa.repository.PayrollRecordRepository;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.StorageService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PayrollRecord} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PayrollRecordServiceImpl extends EntityServiceImpl<PayrollRecordRepository, PayrollRecord, String>
        implements PayrollRecordService {

    @Inject
    private AssetService assetService;

    @Inject
    private StorageService storageService;

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Inject
    @Lazy
    private PayrollDetailDataReadListener readListener;

    @Bean
    public PayrollDetailDataReadListener payrollDetailDataReadListener(
            PayrollDetailService payrollDetailService,
            StaffRepository staffRepository,
            PersonRepository personRepository) {
        return new PayrollDetailDataReadListener(payrollDetailService, staffRepository, personRepository);
    }

    @Override
    public PayrollRecord save(PayrollRecord target) {
        findOneByYearAndMonth(target.getYear(), target.getMonth()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("month", "exists", target.getYear());
            }
        });
        var payroll = super.save(target);
        if (payroll.getSheet() != null && payroll.getImportedAt() == null) {
            final var departmentId = SecurityUtilsExt.getPrimaryDepartmentId();
            final var jobs = jobRoleRepository.findAllByJobDepartmentIdAndRoleCode(departmentId, "worker").stream()
                    .map(JobRole::getJob).toList();
            if (jobs.isEmpty()) {
                throw new InvalidConfigurationException("no job with role worker configed");
            }
            if (jobs.size() > 1) {
                throw new InvalidConfigurationException("multiple jobs with role worker configed");
            }
            readListener.setJob(jobs.get(0));
            readListener.setPayroll(payroll);
            final var sheet = storageService.download(payroll.getSheet());
            final var is = new ByteArrayInputStream(sheet.getData());
            EasyExcel.read(is, PayrollDetailData.class, readListener).sheet().doRead();
            super.save(payroll);
        }
        return payroll;
    }

    @Override
    public void delete(PayrollRecord entity) {
        super.delete(entity);
        if (entity.getSheet() != null) {
            assetService.delete(entity.getSheet());
        }
        if (entity.getSignedSheet() != null) {
            assetService.delete(entity.getSignedSheet());
        }
    }

    @Override
    public Optional<PayrollRecord> findOneByYearAndMonth(int year, int month) {
        return getRepository().findOneByYearAndMonthAndBelongsTo(year, month,
                SecurityUtilsExt.getPrimaryOrganizationId());
    }

    @Override
    public List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getRepository().statistic(organizationIds, startTime, endTime);
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Direction.DESC, "year", "month");
    }

}
