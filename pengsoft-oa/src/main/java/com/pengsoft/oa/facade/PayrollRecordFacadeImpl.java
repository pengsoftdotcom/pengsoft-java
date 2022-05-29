package com.pengsoft.oa.facade;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.excel.EasyExcel;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.PayrollRecordConfirmPicture;
import com.pengsoft.oa.excel.PayrollDetailData;
import com.pengsoft.oa.excel.PayrollDetailDataReadListener;
import com.pengsoft.oa.service.PayrollDetailService;
import com.pengsoft.oa.service.PayrollRecordConfirmPictureService;
import com.pengsoft.oa.service.PayrollRecordService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PayrollRecordFacade}
 *
 * @author peng.dangs@pengsoft.com
 * @since 1.0.0
 */
@Service
public class PayrollRecordFacadeImpl extends EntityFacadeImpl<PayrollRecordService, PayrollRecord, String>
        implements PayrollRecordFacade {

    @Inject
    private PayrollRecordConfirmPictureService confirmPictureService;

    @Inject
    private PayrollDetailService payrollDetailService;

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Inject
    private AssetService assetService;

    @Inject
    @Lazy
    private PayrollDetailDataReadListener readListener;

    @Override
    public PayrollRecord saveWithConfirmPictures(PayrollRecord target, List<Asset> confirmPictures) {
        if (StringUtils.isBlank(target.getId())) {
            super.save(target);
        } else {
            findOne(target.getId()).ifPresent(source -> target.setDetails(source.getDetails()));
        }
        createDetails(target);
        updateConfirmPictures(target, confirmPictures);
        updateDetailConfirmInfo(target);
        return super.save(target);
    }

    private void createDetails(PayrollRecord payroll) {
        if (payroll.getSheet() != null && payroll.getImportedAt() == null) {
            final var departmentId = SecurityUtilsExt.getPrimaryDepartmentId();
            final var jobs = jobRoleRepository.findAllByJobDepartmentIdAndRoleCode(departmentId, "worker").stream()
                    .map(JobRole::getJob).toList();
            if (CollectionUtils.isEmpty(jobs)) {
                throw new InvalidConfigurationException("no job with role worker configed");
            }
            if (jobs.size() > 1) {
                throw new InvalidConfigurationException("multiple jobs with role worker configed");
            }
            readListener.setJob(jobs.get(0));
            readListener.setPayroll(payroll);
            final var sheet = assetService.download(payroll.getSheet());
            final var is = new ByteArrayInputStream(sheet.getData());
            EasyExcel.read(is, PayrollDetailData.class, readListener).sheet().doRead();
        }
    }

    private void updateDetailConfirmInfo(PayrollRecord payroll) {
        final var confirmed = CollectionUtils.isNotEmpty(payroll.getConfirmPictures());
        final var confirmedAt = confirmed ? DateUtils.currentDateTime() : null;
        final var confirmedBy = confirmed ? SecurityUtils.getUserId() : null;
        payroll.getDetails().forEach(detail -> {
            detail.setConfirmedAt(confirmedAt);
            detail.setConfirmedBy(confirmedBy);
            payrollDetailService.save(detail);
        });
        payroll.setConfirmedCount(confirmed ? payroll.getDetails().size() : 0);
    }

    private void updateConfirmPictures(PayrollRecord payroll, List<Asset> pictures) {
        payroll.setConfirmPictures(confirmPictureService.findAllByPayrollRecord(payroll));
        final var source = payroll.getConfirmPictures();
        final var target = pictures.stream().map(asset -> new PayrollRecordConfirmPicture(payroll, asset))
                .toList();
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(s -> EntityUtils.equals(t.getAsset(), s.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(created)) {
            confirmPictureService.save(created);
        }
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(t -> EntityUtils.equals(s.getAsset(), t.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(deleted)) {
            confirmPictureService.delete(source);
            source.removeIf(picture -> deleted.stream().anyMatch(d -> EntityUtils.equals(picture, d)));
        }
        source.addAll(created);
    }

    @Override
    public PayrollRecord deleteSheetByAsset(PayrollRecord payroll, @NotNull Asset asset) {
        if (payroll != null) {
            payroll.setSheet(null);
            payroll.setImportedAt(null);
            payroll.getDetails().forEach(payrollDetailService::delete);
            payroll.getDetails().clear();
            payroll.setPaidCount(0);
            payroll.setConfirmedCount(0);
            payroll = super.save(payroll);
        }
        assetService.delete(asset);
        return payroll;
    }

    @Override
    public PayrollRecord deleteConfirmPictureByAsset(PayrollRecord payroll, Asset target) {
        if (payroll == null) {
            assetService.delete(target);
        } else {
            final var confirmPictures = payroll.getConfirmPictures();
            final var optional = confirmPictures.stream()
                    .filter(source -> EntityUtils.equals(source.getAsset(), target)).findFirst();
            if (optional.isPresent()) {
                final var confirmPicture = optional.get();
                confirmPictures.remove(confirmPicture);
                updateDetailConfirmInfo(payroll);
                payroll = super.save(payroll);
                confirmPictureService.delete(confirmPicture);
            } else {
                assetService.delete(target);
            }
        }
        return payroll;
    }

    @Override
    public void delete(PayrollRecord payrollRecord) {
        confirmPictureService.delete(payrollRecord.getConfirmPictures());
        super.delete(payrollRecord);
    }

    @Override
    public Optional<PayrollRecord> findOneByYearAndMonthAndBelongsTo(int year, int month, @NotBlank String belongsTo) {
        return getService().findOneByYearAndMonthAndBelongsTo(year, month, belongsTo);
    }

    @Override
    public List<Map<String, Object>> statistic(@NotEmpty List<String> organizationIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getService().statistic(organizationIds, startTime, endTime);
    }

}