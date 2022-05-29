package com.pengsoft.oa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.excel.PayrollDetailDataReadListener;
import com.pengsoft.oa.repository.PayrollRecordRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.repository.DictionaryItemRepository;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Bean;
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

    private static final String PAYROLL_RECORD_STATUS = "payroll_record_status";

    @Inject
    private AssetService assetService;

    @Inject
    private DictionaryItemRepository dictionaryItemRepository;

    @Bean
    public PayrollDetailDataReadListener payrollDetailDataReadListener(
            PayrollDetailService payrollDetailService,
            StaffRepository staffRepository,
            PersonRepository personRepository) {
        return new PayrollDetailDataReadListener(payrollDetailService, staffRepository, personRepository);
    }

    @Override
    public PayrollRecord save(PayrollRecord target) {
        findOneByYearAndMonthAndBelongsTo(target.getYear(), target.getMonth(),
                StringUtils.defaultString(target.getBelongsTo(), SecurityUtilsExt.getPrimaryOrganizationId()))
                .ifPresent(source -> {
                    if (EntityUtils.notEquals(source, target)) {
                        throw getExceptions().constraintViolated("month", "exists", target.getMonth());
                    }
                });
        updateStatus(target);
        return super.save(target);
    }

    private void updateStatus(PayrollRecord payroll) {
        if (payroll.getDetails().isEmpty()) {
            if (payroll.getCreatedAt() != null
                    && DateUtils.currentDateTime().getDayOfYear() - payroll.getCreatedAt().getDayOfYear() > 5) {
                dictionaryItemRepository.findOneByTypeCodeAndParentIdAndCode(PAYROLL_RECORD_STATUS, null, "arrears")
                        .ifPresent(payroll::setStatus);
            } else {
                dictionaryItemRepository.findOneByTypeCodeAndParentIdAndCode(PAYROLL_RECORD_STATUS, null, "unpaid")
                        .ifPresent(payroll::setStatus);
            }
        } else {
            dictionaryItemRepository.findOneByTypeCodeAndParentIdAndCode(PAYROLL_RECORD_STATUS, null, "paid")
                    .ifPresent(payroll::setStatus);
        }
    }

    @Override
    public void delete(PayrollRecord payroll) {
        super.delete(payroll);
        if (payroll.getSheet() != null) {
            assetService.delete(payroll.getSheet());
        }
        if (payroll.getSignedSheet() != null) {
            assetService.delete(payroll.getSignedSheet());
        }
    }

    @Override
    public Optional<PayrollRecord> findOneByYearAndMonthAndBelongsTo(int year, int month,
            @NotBlank String belongsTo) {
        return getRepository().findOneByYearAndMonthAndBelongsTo(year, month, belongsTo);
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
