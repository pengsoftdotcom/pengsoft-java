package com.pengsoft.oa.messaging;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.domain.QPayrollDetail;
import com.pengsoft.oa.service.PayrollDetailService;
import com.pengsoft.security.domain.User;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.messaging.AbstractMessageBuilder;
import com.pengsoft.system.messaging.MessageBuilder;

/**
 * {@link MessageBuilder} for payroll detail confirmation.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class PayrollDetailConfirmMessageBuilder extends AbstractMessageBuilder {

    @Inject
    private PayrollDetailService payrollDetailService;

    @Override
    protected String getTemplateCode() {
        return "payroll-detail-confirm";
    }

    @Override
    protected List<User> getReceivers(Object[] args, Object result) {
        final var payroll = (PayrollRecord) args[0];
        return payrollDetailService.findOne(QPayrollDetail.payrollDetail.payroll.id.eq(payroll.getId())).stream()
                .map(PayrollDetail::getStaff)
                .map(Staff::getPerson)
                .map(Person::getUser).toList();

    }

    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        final var messages = super.build(args, result, types);
        messages.entrySet().stream().flatMap(entry -> entry.getValue().stream())
                .forEach(message -> message.setParams(Map.of("thing1", "test", "phrase2", "待确认")));
        return messages;
    }

}
