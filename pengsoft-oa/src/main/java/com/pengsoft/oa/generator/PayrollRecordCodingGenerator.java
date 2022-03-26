package com.pengsoft.oa.generator;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.generator.CodingGenerator;
import com.pengsoft.basedata.service.CodingRuleService;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

/**
 * Coding generator for {@link PayrollRecord}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class PayrollRecordCodingGenerator implements CodingGenerator {

    @Inject
    private CodingRuleService service;

    @Override
    public String generate(CodingRule codingRule) {
        final var date = DateUtils.currentDate();
        String value = codingRule.getPrefix() + date.getYear() + String.format("%02d", date.getMonthValue());
        if (StringUtils.notEquals(codingRule.getValue(), value)) {
            codingRule.setValue(value);
            service.save(codingRule);
        }
        return value;
    }

}
