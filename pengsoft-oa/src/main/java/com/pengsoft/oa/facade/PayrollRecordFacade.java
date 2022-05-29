package com.pengsoft.oa.facade;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.PayrollRecordConfirmPicture;
import com.pengsoft.oa.service.PayrollRecordService;
import com.pengsoft.support.facade.EntityFacade;
import com.pengsoft.system.domain.Asset;

/**
 * The facade interface of {@link PayrollRecord}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PayrollRecordFacade
        extends EntityFacade<PayrollRecordService, PayrollRecord, String>, PayrollRecordService {

    /**
     * Save with pictures
     * 
     * @param payrollRecord   {@link PayrollRecord}
     * @param confirmPictures {@link Asset}
     */
    PayrollRecord saveWithConfirmPictures(@Valid PayrollRecord payrollRecord, List<Asset> confirmPictures);

    /**
     * delete the payroll record sheet by given {@link Asset}
     * 
     * @param payrollRecord {@link PayrollRecord}
     * @param asset         {@link Asset}
     */
    PayrollRecord deleteSheetByAsset(PayrollRecord payrollRecord, @NotNull Asset asset);

    /**
     * delete {@link PayrollRecordConfirmPicture} by given {@link Asset}
     * 
     * @param payrollRecord {@link PayrollRecord}
     * @param asset         {@link Asset}
     */
    PayrollRecord deleteConfirmPictureByAsset(PayrollRecord payrollRecord, @NotNull Asset asset);

}
