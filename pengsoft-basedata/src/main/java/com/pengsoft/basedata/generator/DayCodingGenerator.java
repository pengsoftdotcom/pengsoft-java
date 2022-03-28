package com.pengsoft.basedata.generator;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.service.CodingRuleService;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

/**
 * Coding generator base on current day.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class DayCodingGenerator implements CodingGenerator {

    @Inject
    private CodingRuleService service;

    @Override
    public String generate(CodingRule codingRule) {
        final var value = new StringBuilder();
        value.append(StringUtils.defaultString(codingRule.getPrefix(), ""));
        final var date = DateUtils.currentDate();
        value.append(date.getYear());
        value.append(String.format("%02d", date.getMonthValue()));
        value.append(String.format("%02d", date.getDayOfMonth()));
        if (codingRule.getLength() > 0) {
            codingRule.setIndex(codingRule.getIndex() + codingRule.getStep());
            final var format = "%0" + codingRule.getLength() + "d";
            value.append(String.format(format, codingRule.getIndex()));
        }
        value.append(StringUtils.defaultString(codingRule.getSuffix(), ""));
        if (StringUtils.notEquals(codingRule.getValue(), value)) {
            codingRule.setValue(value.toString());
            service.save(codingRule);
        }
        return value.toString();
    }

}
