package com.pengsoft.system.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.SystemParam;

/**
 * The service interface of {@link SystemParam}.
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SystemParamService extends EntityService<SystemParam, String> {

    /**
     * 根据编码查询单个{@link SystemParam}
     * 
     * @param code 编码
     */
    Optional<SystemParam> findOneByCode(@NotBlank String code);
}
