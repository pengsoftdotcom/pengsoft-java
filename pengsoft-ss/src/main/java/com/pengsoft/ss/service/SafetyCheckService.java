package com.pengsoft.ss.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

/**
 * The service interface of {@link SafetyCheck}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyCheckService extends EntityService<SafetyCheck, String> {

    /**
     * 提交
     * 
     * @param check  {@link SafetyCheck}
     * @param assets A collection of {@link Asset}
     */
    void submit(@Valid @NotNull SafetyCheck check, @NotEmpty List<Asset> assets);

    /**
     * 处理
     * 
     * @param check  {@link SafetyCheck}
     * @param result 处理结果
     * @param assets A collection of {@link Asset}
     */
    void handle(@NotNull SafetyCheck check, @NotBlank String result, @NotEmpty List<Asset> assets);

    /**
     * Returns an {@link Optional} of a {@link SafetyCheck}
     * with the given
     * code.
     *
     * @param code {@link SafetyCheck}'s code
     */
    Optional<SafetyCheck> findOneByCode(@NotBlank String code);

}
