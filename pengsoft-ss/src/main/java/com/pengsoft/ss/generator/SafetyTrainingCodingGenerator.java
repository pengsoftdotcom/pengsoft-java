package com.pengsoft.ss.generator;

import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.generator.CodingGenerator;
import com.pengsoft.basedata.service.CodingRuleService;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

/**
 * Coding generator for {@link SafetyTraining}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class SafetyTrainingCodingGenerator implements CodingGenerator {

    @Inject
    private CodingRuleService service;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String generate(CodingRule codingRule) {
        String value = codingRule.getPrefix() + formatter.format(DateUtils.currentDate());
        if (StringUtils.notEquals(codingRule.getValue(), value)) {
            codingRule.setValue(value);
            service.save(codingRule);
        }
        return value;
    }

}
