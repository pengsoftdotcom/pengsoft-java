package com.pengsoft.basedata.generator;

import com.pengsoft.basedata.domain.CodingRule;

/**
 * 编码生成器
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface CodingGenerator {

    String generate(CodingRule codingRule);

}
