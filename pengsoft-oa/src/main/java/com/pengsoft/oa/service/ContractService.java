package com.pengsoft.oa.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Contract}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ContractService extends EntityService<Contract, String> {

    /**
     * Confirmsthe contract is real.
     * 
     * @param contract {@link Contract}
     */
    void confirm(@NotNull Contract contract);

}
