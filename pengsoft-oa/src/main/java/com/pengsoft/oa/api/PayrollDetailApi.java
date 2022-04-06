package com.pengsoft.oa.api;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.QPayrollDetail;
import com.pengsoft.oa.service.PayrollDetailService;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link PayrollDetail}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/oa/payroll-detail")
public class PayrollDetailApi extends EntityApi<PayrollDetailService, PayrollDetail, String> {

    @TaskHandler(name = "payrollDetailConfirmTaskHandler", finish = true)
    @PutMapping("confirm")
    public void confirm(@RequestParam("id") PayrollDetail payrollRecord) {
        getService().confirm(payrollRecord);
    }

    @TaskHandler(name = "payrollDetailConfirmTaskHandler", finish = true)
    @Authorized
    @PutMapping("confirm-mine")
    public void confirmMine(String id) {
        getService().findOne(id).ifPresent(getService()::confirm);
    }

    @Authorized
    @GetMapping("find-one-of-mine")
    public PayrollDetail findOneOfMine(String id) {
        return super.findOne(getService().findOne(id).orElse(null));
    }

    @Authorized
    @GetMapping("find-page-of-mine")
    public Page<PayrollDetail> findPageOfMine(Predicate predicate, Pageable pageable) {
        predicate = QueryDslUtils.merge(predicate,
                QPayrollDetail.payrollDetail.staff.id.eq(SecurityUtilsExt.getStaffId()));
        return super.findPage(predicate, pageable);
    }

}