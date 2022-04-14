package com.pengsoft.ss.facade;

import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.QualityCheck;
import com.pengsoft.ss.service.QualityCheckService;
import com.pengsoft.support.facade.EntityFacade;
import com.pengsoft.system.domain.Asset;

import org.apache.http.annotation.Contract;

/**
 * The facade interface of {@link QualityCheck}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface QualityCheckFacade
        extends EntityFacade<QualityCheckService, QualityCheck, String>, QualityCheckService {

    /**
     * delete {@link QualityCheck} by given {@link Asset}
     * 
     * @param check {@link Contract}
     * @param asset {@link Asset}
     */
    void deleteFileByAsset(@NotNull QualityCheck check, @NotNull Asset asset);

}
