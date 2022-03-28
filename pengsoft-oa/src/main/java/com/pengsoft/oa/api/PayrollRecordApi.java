package com.pengsoft.oa.api;

import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.service.PayrollRecordService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.annotation.Messaging;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link PayrollDetail}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/oa/payroll-record")
public class PayrollRecordApi extends EntityApi<PayrollRecordService, PayrollRecord, String> {

    @Messaging(builder = "payrollDetailConfirmMessageBuilder")
    @Override
    public void save(@RequestBody PayrollRecord entity) {
        super.save(entity);
    }

}