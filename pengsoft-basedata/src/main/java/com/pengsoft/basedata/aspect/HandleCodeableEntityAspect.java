package com.pengsoft.basedata.aspect;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.generator.CodingGenerator;
import com.pengsoft.basedata.service.CodingRuleService;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.util.ClassUtils;
import com.pengsoft.support.util.StringUtils;

import org.aspectj.lang.JoinPoint;
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
    public void handle(JoinPoint jp, Object result) {
        final var apiClass = jp.getTarget().getClass();
        final var entityClass = ClassUtils.getSuperclassGenericType(apiClass, 1);
        if (Codeable.class.isAssignableFrom(entityClass) && StringUtils.isBlank(getCode(result))) {
            service.findOneByEntity(entityClass.getSimpleName()).ifPresent(codingRule -> {
                final var generator = context.getBean(codingRule.getGenerator(), CodingGenerator.class);
                setCode(result, generator.generate(codingRule));
            });
        }
    }

    @SuppressWarnings("unchecked")
    public String getCode(Object result) {
        if (result instanceof Codeable entity) {
            return entity.getCode();
        } else {
            return (String) ((Map<String, Object>) result).get("code");
        }
    }

    @SuppressWarnings("unchecked")
    public void setCode(Object result, String code) {
        if (result instanceof Codeable entity) {
            entity.setCode(code);
        } else {
            ((Map<String, Object>) result).put("code", code);
        }
    }

}
