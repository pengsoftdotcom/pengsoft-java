package com.pengsoft.security.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.aspect.JoinPoints;
import com.pengsoft.support.util.ClassUtils;

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
public class HandleDataAuthorityAspect {

    @Inject
    private ApiMethodArgsDataAuthorityHandler handler;

    @SuppressWarnings("unchecked")
    @Around(JoinPoints.ALL_API)
    public Object handle(final ProceedingJoinPoint jp) throws Throwable {
        final var apiClass = jp.getTarget().getClass();
        final var entityClass = (Class<? extends OwnedEntityImpl>) ClassUtils.getSuperclassGenericType(apiClass, 1);
        final var method = ClassUtils.getPublicMethod(apiClass, jp.getSignature().getName(),
                (((MethodSignature) jp.getSignature()).getMethod()).getParameterTypes());
        if (SecurityUtils.isAuthenticated() && handler.support(entityClass)) {
            return jp.proceed(handler.handle(entityClass, getAuthorized(apiClass, method), jp.getArgs()));
        } else {
            return jp.proceed(jp.getArgs());
        }
    }

    private Authorized getAuthorized(Class<?> apiClass, Method method) {
        if (method.isAnnotationPresent(Authorized.class)) {
            return method.getAnnotation(Authorized.class);
        }
        if (apiClass.isAnnotationPresent(Authorized.class)) {
            apiClass.getAnnotation(Authorized.class);
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

}
