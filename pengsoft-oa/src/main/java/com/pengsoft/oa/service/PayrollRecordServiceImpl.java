package com.pengsoft.oa.service;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import javax.inject.Inject;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.excel.PayrollDetailData;
import com.pengsoft.oa.excel.PayrollDetailDataReadListener;
import com.pengsoft.oa.repository.PayrollDetailRepository;
import com.pengsoft.oa.repository.PayrollRecordRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.StorageService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
    private PayrollDetailRepository payrollDetailRepository;

    @Inject
    private AssetService assetService;

    @Inject
    private StorageService storageService;

    @Bean
    public PayrollDetailDataReadListener payrollDetailDataReadListener() {
        return new PayrollDetailDataReadListener();
    }

    @Override
    public PayrollRecord save(PayrollRecord target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        super.save(target);
        if (StringUtils.isBlank(target.getId())) {
            final var sheet = storageService.download(target.getSheet());
            EasyExcel.read(new ByteArrayInputStream(sheet.getData()), PayrollDetailData.class,
                    payrollDetailDataReadListener()).sheet().doRead();
        }
        return target;
    }

    @Override
    public void delete(PayrollRecord entity) {
        super.delete(entity);
        assetService.delete(entity.getSheet());
        assetService.delete(entity.getSignedSheet());
    }

    @Override
    public Optional<CodingRule> findOneByCode(String code) {
        return getRepository().findOneByCodeAndBelongsTo(code, SecurityUtilsExt.getPrimaryOrganizationId());
    }

}
