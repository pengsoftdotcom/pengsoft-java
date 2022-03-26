package com.pengsoft.oa.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Contract}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ContractPictureService extends EntityService<ContractPicture, String> {

    /**
     * Returns all {@link ContractPicture}s with the given contract.
     * 
     * @param contract The {@link Contract}.
     */
    List<ContractPicture> findAllByContract(@NotNull Contract contract);

}
