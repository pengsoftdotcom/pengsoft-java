package com.pengsoft.ss.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

/**
 * The service interface of {@link SafetyTraining}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingService extends EntityService<SafetyTraining, String> {

    /**
     * 提交
     * 
     * @param training {@link SafetyTraining}
     */
    void saveAndSubmit(@Valid @NotNull SafetyTraining training);

    /**
     * 提交
     * 
     * @param training {@link SafetyTraining}
     */
    void submit(@NotNull SafetyTraining training);

    /**
     * 开始
     * 
     * @param training {@link SafetyTraining}
     */
    void start(@NotNull SafetyTraining training);

    /**
     * 结束，并上传过程拍照
     * 
     * @param training {@link SafetyTraining}
     * @param files    培训过程照片
     */
    void end(@NotNull SafetyTraining training, @NotEmpty List<Asset> files);

    /**
     * Returns an {@link Optional} of a {@link SafetyTraining}
     * with the given
     * code.
     *
     * @param code {@link SafetyTraining}'s code
     */
    Optional<SafetyTraining> findOneByCode(@NotBlank String code);

}
