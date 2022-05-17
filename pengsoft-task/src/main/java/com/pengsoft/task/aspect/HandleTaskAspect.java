package com.pengsoft.task.aspect;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.support.util.StringUtils;
import com.pengsoft.task.annotation.TaskHandler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
@Aspect
public class HandleTaskAspect {

    @Inject
    private ApplicationContext context;

    @AfterReturning(pointcut = "@annotation(handler)", returning = "result")
    public void handle(JoinPoint jp, TaskHandler handler, Object result) {
        final var args = jp.getArgs();
        if (StringUtils.isNotBlank(handler.name()) && context.containsBean(handler.name())) {
            final var executor = context.getBean(handler.name(), TaskExecutor.class);
            if (handler.create()) {
                try {
                    executor.create(args, result);
                } catch (Exception e) {
                    log.error("task create error: {} {}", handler.name(), e.getMessage());
                }
            }
            if (handler.finish()) {
                try {
                    executor.finish(args, result);
                } catch (Exception e) {
                    log.error("task finish error: {} {}", handler.name(), e.getMessage());
                }
            }

            if (handler.delete()) {
                try {
                    executor.delete(args, result);
                } catch (Exception e) {
                    log.error("task delete error: {} {}", handler.name(), e.getMessage());
                }
            }
        }
    }

}
