package com.pengsoft.ss.facade;

import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.ss.service.SafetyTrainingService;
import com.pengsoft.support.facade.EntityFacade;
import com.pengsoft.system.domain.Asset;

import org.apache.http.annotation.Contract;

/**
 * The facade interface of {@link SafetyTraining}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingFacade
        extends EntityFacade<SafetyTrainingService, SafetyTraining, String>, SafetyTrainingService {

    /**
     * delete {@link SafetyTrainingFile} by given {@link Asset}
     * 
     * @param check {@link Contract}
     * @param asset {@link Asset}
     */
    void deleteFileByAsset(@NotNull SafetyTraining check, @NotNull Asset asset);

    /**
     * delete {@link deleteConfirmFileByAsset} by given {@link Asset}
     * 
     * @param check {@link Contract}
     * @param asset {@link Asset}
     */
    void deleteConfirmFileByAsset(@NotNull SafetyTraining check, @NotNull Asset asset);

}
