package com.pengsoft.oa.api;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.QPayrollDetail;
import com.pengsoft.oa.service.PayrollDetailService;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The web api of {@link PayrollDetail}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/oa/payroll-detail")
public class PayrollDetailApi extends EntityApi<PayrollDetailService, PayrollDetail, String> {

    @Inject
    private StaffService staffService;

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

    @Override
    public Page<PayrollDetail> findPage(Predicate predicate, Pageable pageable) {
        return super.findPage(getQueryPredicate(predicate), pageable);
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
        return super.findPage(getQueryPredicate(predicate), pageable);
    }

    private Predicate getQueryPredicate(Predicate predicate) {
        final var request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest();
        final var keyword = request.getParameter("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            final var root = QStaff.staff.person;
            final var staffIds = staffService.findAll(
                    root.name.contains(keyword)
                            .or(root.mobile.contains(keyword).or(root.identityCardNumber.contains(keyword))),
                    Sort.unsorted()).stream().map(Staff::getId).toList();
            predicate = QueryDslUtils.merge(predicate, QPayrollDetail.payrollDetail.staff.id.in(staffIds));
        }
        return predicate;
    }

}