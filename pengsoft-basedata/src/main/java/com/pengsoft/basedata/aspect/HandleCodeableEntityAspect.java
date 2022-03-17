package com.pengsoft.basedata.aspect;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.generator.CodingGenerator;
import com.pengsoft.basedata.service.CodingRuleService;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.util.StringUtils;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;

/**
 * 处理可编码实体切面
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Aspect
public class HandleCodeableEntityAspect {

    @Inject
    private ApplicationContext context;

    @Inject
    private CodingRuleService service;

    @AfterReturning(pointcut = "execution(public * com.pengsoft..api.*Api.findOne*(..))", returning = "result")
    public void handle(Object result) {
        if (result instanceof Codeable entity && StringUtils.isBlank(entity.getCode())) {
            service.findOneByEntity(entity.getClass().getSimpleName()).ifPresent(codingRule -> {
                final var generator = context.getBean(codingRule.getGenerator(), CodingGenerator.class);
                entity.setCode(generator.generate(codingRule));
            });
        }
    }

}
