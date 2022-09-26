package com.pengsoft.security.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.querydsl.core.types.Predicate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.access.AccessDeniedException;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * API数据权限处理器抽象类
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public abstract class AbstractApiDataAuthorityHandler<T extends EntityImpl>
        implements ApiDataAuthorityHandler<T, String> {

    @Getter
    @Inject
    private Repositories repositories;

    @Getter
    @Inject
    private Exceptions exceptions;

    @Override
    public Object[] handle(Class<T> entityClass, Method method, Object[] args) throws AccessDeniedException {
        return Arrays.stream(args).map(arg -> {
            if (arg instanceof Predicate predicate) {
                return exchange(entityClass, method, predicate);
            } else if (arg != null && entityClass.isAssignableFrom(arg.getClass())
                    && !isAuthorized(method, getEntity(entityClass.cast(arg)))) {
                throw new AccessDeniedException("Not authorized");
            } else if (arg instanceof Collection<?> entities
                    && entities.stream().allMatch(entityClass::isInstance)
                    && !entities.stream().map(entityClass::cast)
                            .allMatch(entity -> isAuthorized(method, entity))) {
                throw new AccessDeniedException("Not authorized");
            } else {
                return arg;
            }
        }).toArray();
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private T getEntity(final T entity) {
        if (entity.getId() != null && entity.getCreatedAt() == null) {
            final var entityClass = entity.getClass();
            final var repository = repositories.getRepositoryFor(entityClass).map(CrudRepository.class::cast)
                    .orElseThrow(() -> new InvalidConfigurationException("No repository for " + entityClass.getName()));
            return (T) repository.findById(entity.getId())
                    .orElseThrow(() -> exceptions.entityNotExists(entityClass, entity.getId()));
        } else {
            return entity;
        }
    }

    protected abstract Predicate exchange(Class<T> entityClass, Method method, Predicate predicate);

    protected abstract boolean isAuthorized(Method method, T entity);

}
