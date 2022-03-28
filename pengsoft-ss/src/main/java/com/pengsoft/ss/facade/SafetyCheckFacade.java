package com.pengsoft.ss.facade;

import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.service.SafetyCheckService;
import com.pengsoft.support.facade.EntityFacade;
import com.pengsoft.system.domain.Asset;

import org.apache.http.annotation.Contract;

/**
 * The facade interface of {@link SafetyCheck}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyCheckFacade extends EntityFacade<SafetyCheckService, SafetyCheck, String>, SafetyCheckService {

    /**
     * delete {@link SafetyCheck} by given {@link Asset}
     * 
     * @param check {@link Contract}
     * @param asset {@link Asset}
     */
    void deleteFileByAsset(@NotNull SafetyCheck check, @NotNull Asset asset);

}
