package com.pengsoft.basedata.generator;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.service.CodingRuleService;

/**
 * 默认的编码生成器
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class DefaultCodingGenerator implements CodingGenerator {

    @Inject
    private CodingRuleService service;

    @Override
    public String generate(CodingRule codingRule) {
        final var index = codingRule.getIndex() + codingRule.getStep();
        final var format = "%0" + codingRule.getLength() + "d";
        final var value = new StringBuilder();
        value.append(codingRule.getPrefix());
        value.append(String.format(format, index));
        value.append(codingRule.getSuffix());
        codingRule.setValue(value.toString());
        service.save(codingRule);
        return value.toString();
    }

}
