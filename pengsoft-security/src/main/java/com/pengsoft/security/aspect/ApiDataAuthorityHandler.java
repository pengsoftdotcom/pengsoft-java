package com.pengsoft.security.aspect;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.pengsoft.support.domain.Entity;

import org.springframework.security.access.AccessDeniedException;

/**
 * API参数数据权限处理器
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ApiDataAuthorityHandler<T extends Entity<ID>, ID extends Serializable> {

    /**
     * 是否支持添加数据权限
     * 
     * @param entityClass 实体类
     */
    boolean support(Class<?> entityClass);

    /**
     * 返回添加了数据权限的参数
     * 
     * @param entityClass API所属实体类
     * @param method      API方法
     * @param args        Api方法参数
     * @throws AccessDeniedException
     */
    Object[] handle(Class<T> entityClass, Method method, Object[] args) throws AccessDeniedException;

}
