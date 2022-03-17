package com.pengsoft.security.aspect;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.OwnedEntityImpl;

import org.springframework.security.access.AccessDeniedException;

/**
 * API参数数据权限处理器
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ApiMethodArgsDataAuthorityHandler {

    /**
     * 是否支持添加数据权限
     * 
     * @param entityClass 实体类
     */
    boolean support(Class<?> entityClass);

    /**
     * 返回添加了数据权限的参数
     * 
     * @param args Api方法参数
     * @throws AccessDeniedException
     */
    Object[] handle(Class<? extends OwnedEntityImpl> entityClass, Authorized authorized, Object[] args)
            throws AccessDeniedException;

}
