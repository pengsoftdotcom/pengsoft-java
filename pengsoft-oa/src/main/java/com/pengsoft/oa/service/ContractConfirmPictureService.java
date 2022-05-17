package com.pengsoft.oa.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractConfirmPicture;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Contract}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ContractConfirmPictureService extends EntityService<ContractConfirmPicture, String> {

    /**
     * Returns all {@link ContractConfirmPicture}s with the given contract.
     * 
     * @param contract The {@link Contract}.
     */
    List<ContractConfirmPicture> findAllByContract(@NotNull Contract contract);

}
