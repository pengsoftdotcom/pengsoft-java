package com.pengsoft.oa.service;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.PayrollRecordConfirmPicture;
import com.pengsoft.oa.repository.PayrollRecordConfirmPictureRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PayrollRecordConfirmPicture} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PayrollRecordConfirmPictureServiceImpl
        extends EntityServiceImpl<PayrollRecordConfirmPictureRepository, PayrollRecordConfirmPicture, String>
        implements PayrollRecordConfirmPictureService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(PayrollRecordConfirmPicture payrollRecordConfirmPicture) {
        super.delete(payrollRecordConfirmPicture);
        assetService.delete(payrollRecordConfirmPicture.getAsset());
    }

    @Override
    public List<PayrollRecordConfirmPicture> findAllByPayrollRecord(PayrollRecord payrollRecord) {
        return getRepository().findAllByPayrollId(payrollRecord.getId());
    }

}
