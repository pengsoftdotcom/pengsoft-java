package com.pengsoft.basedata.generator;

import java.time.format.DateTimeFormatter;

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

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String generate(CodingRule codingRule) {
        final var value = new StringBuilder();
        value.append(StringUtils.defaultString(codingRule.getPrefix(), ""));
        final var prevDate = getPrevDate(codingRule);
        final var nextDate = getNextDate();
        value.append(nextDate);
        if (codingRule.getLength() > 0) {
            final var index = (StringUtils.equals(prevDate, nextDate) ? codingRule.getIndex() : 0)
                    + codingRule.getStep();
            codingRule.setIndex(index);
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

    private String getPrevDate(CodingRule codingRule) {
        String value = codingRule.getValue();
        if (StringUtils.isNotBlank(value)) {
            return value.replaceFirst(codingRule.getPrefix(), "").substring(0, 8);
        }
        return null;
    }

    private String getNextDate() {
        return DateUtils.currentDate().format(formatter);
    }

}
