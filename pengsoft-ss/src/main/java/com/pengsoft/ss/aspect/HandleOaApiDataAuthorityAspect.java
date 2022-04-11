package com.pengsoft.ss.aspect;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.ClassUtils;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.PredicateOperation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 处理OA API数据权限切面
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
@Aspect
public class HandleOaApiDataAuthorityAspect {

    @Inject
    private ConstructionProjectService constructionProjectService;

    @Inject
    private Exceptions exceptions;

    @Around("execution(public * com.pengsoft.oa..api.*Api.findPage*(..))")
    public Object handle(final ProceedingJoinPoint jp) throws Throwable {
        final var request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        final var projectId = request.getParameter("project.id");
        final var args = Arrays.stream(jp.getArgs()).map(arg -> {
            if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER) && StringUtils.isNotBlank(projectId)
                    && arg instanceof Predicate predicate) {
                final var predicates = new ArrayList<Predicate>();
                predicates.add(getAuthorityPredicate(jp, projectId));
                addQueryPredicates(predicate, predicates);
                return ExpressionUtils.allOf(predicates);
            } else {
                return arg;
            }
        }).toArray();
        return jp.proceed(args);
    }

    @SuppressWarnings("rawtypes")
    private void addQueryPredicates(Predicate predicate, final ArrayList<Predicate> predicates) {
        final var predicateOperation = ExpressionUtils.extract(predicate);
        if (predicateOperation != null && predicateOperation instanceof PredicateOperation po) {
            final var queue = new ArrayDeque<PredicateOperation>();
            queue.push(po);
            while (!queue.isEmpty()) {
                final var current = queue.pop();
                current.getArgs().forEach(sarg -> {
                    if (sarg instanceof PredicateOperation spo) {
                        queue.push(spo);
                    }
                    if (sarg instanceof PathImpl path && !ArrayUtils
                            .contains(new String[] { "controlledBy", "belongsTo" }, path.getMetadata().getName())) {
                        predicates.add(current);
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    private BooleanExpression getAuthorityPredicate(final ProceedingJoinPoint jp, final String projectId) {
        final var project = constructionProjectService.findOne(projectId)
                .orElseThrow(() -> exceptions.entityNotExists(ConstructionProject.class, "id"));
        final var apiClass = jp.getTarget().getClass();
        final var entityClass = (Class<? extends EntityImpl>) ClassUtils.getSuperclassGenericType(apiClass, 1);
        return ((StringPath) QueryDslUtils.getPath(entityClass, "controlledBy"))
                .eq(project.getBuManager().getDepartment().getId());
    }

}
