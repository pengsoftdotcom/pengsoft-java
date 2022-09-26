package com.pengsoft.security.aspect;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.aspect.JoinPoints;
import com.pengsoft.support.config.properties.TransactionProperties;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.util.ClassUtils;
import com.pengsoft.support.util.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Handle API data authorities from outside callers.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Aspect
public class HandleApiDataAuthorityAspect<T extends Entity<ID>, ID extends Serializable> {

    @Inject
    private List<ApiDataAuthorityHandler<T, ID>> handlers;

    @Inject
    private TransactionProperties properties;

    @SuppressWarnings("unchecked")
    @Around(JoinPoints.ALL_API)
    public Object handle(final ProceedingJoinPoint jp) throws Throwable {
        final var apiClass = jp.getTarget().getClass();
        final var entityClass = (Class<T>) ClassUtils.getSuperclassGenericType(apiClass, 1);
        final var method = ClassUtils.getPublicMethod(apiClass, jp.getSignature().getName(),
                (((MethodSignature) jp.getSignature()).getMethod()).getParameterTypes());
        final var readonlyMethodNames = properties.getReadonly().stream()
                .map(readonly -> readonly.substring(0, readonly.length() - 1)).toArray(String[]::new);
        final var isReadonlyMethod = StringUtils.startsWithAny(method.getName(), readonlyMethodNames);
        final var authorized = getAuthorized(apiClass, method);
        final var isAuthorized = hasAdminRole(entityClass)
                || (isReadonlyMethod && isReadable(authorized, entityClass))
                || (!isReadonlyMethod && isWritable(authorized, entityClass));
        if (SecurityUtils.isAuthenticated() && !isAuthorized) {
            final var handler = handlers.stream().filter(h -> h.support(entityClass)).findFirst().orElse(null);
            if (handler != null) {
                return jp.proceed(handler.handle(entityClass, method, jp.getArgs()));
            }
        }
        return jp.proceed(jp.getArgs());
    }

    private Authorized getAuthorized(Class<?> apiClass, Method method) {
        if (method.isAnnotationPresent(Authorized.class)) {
            return method.getAnnotation(Authorized.class);
        }
        if (apiClass.isAnnotationPresent(Authorized.class)) {
            return apiClass.getAnnotation(Authorized.class);
        }
        return new Authorized() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Authorized.class;
            }

            @Override
            public boolean readable() {
                return false;
            }

            @Override
            public boolean writable() {
                return false;
            }

        };
    }

    private boolean hasAdminRole(final Class<T> entityClass) {
        final var roleCodes = new ArrayList<String>();
        roleCodes.add(Role.ADMIN);
        if (entityClass != null) {
            roleCodes.add(SecurityUtils.getModuleAdminRoleCode(entityClass));
            roleCodes.add(SecurityUtils.getEntityAdminRoleCode(entityClass));
        }
        return SecurityUtils.hasAnyRole(roleCodes.toArray(String[]::new));
    }

    private boolean isReadable(Authorized authorized, Class<?> entityClass) {
        return Optional.ofNullable(entityClass)
                .map(clazz -> clazz.getAnnotation(Authorized.class))
                .orElse(authorized)
                .readable();
    }

    private boolean isWritable(Authorized authorized, Class<?> entityClass) {
        return Optional.ofNullable(entityClass)
                .map(clazz -> clazz.getAnnotation(Authorized.class))
                .orElse(authorized)
                .writable();
    }

}
