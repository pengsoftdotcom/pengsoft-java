package com.pengsoft.oa.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.QPayrollRecord;
import com.pengsoft.oa.service.PayrollRecordService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping(Constant.API_PREFIX + "/oa/payroll-record")
public class PayrollRecordApi extends EntityApi<PayrollRecordService, PayrollRecord, String> {

    @TaskHandler(name = "payrollDetailConfirmTaskHandler", create = true)
    @Messaging(builder = "payrollDetailConfirmMessageBuilder")
    @Override
    public void save(@RequestBody PayrollRecord entity) {
        super.save(entity);
    }

    @Override
    public Page<PayrollRecord> findPage(Predicate predicate, Pageable pageable) {
        final var root = QPayrollRecord.payrollRecord;
        final var request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        final var startTime = request.getParameter("startTime");
        final var endTime = request.getParameter("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            predicate = QueryDslUtils.merge(predicate,
                    root.createdAt.between(DateUtils.parseDateTime(startTime), DateUtils.parseDateTime(endTime)));
        }
        return super.findPage(predicate, pageable);
    }

    @GetMapping("statistic")
    public List<Map<String, Object>> statistic(
            @RequestParam(value = "organization.id", required = false) List<String> organizationIds,
            LocalDateTime startTime, LocalDateTime endTime) {
        return getService().statistic(organizationIds, startTime, endTime);
    }

}