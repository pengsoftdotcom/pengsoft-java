package com.pengsoft.oa.facade;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.service.ContractService;
import com.pengsoft.support.facade.EntityFacade;
import com.pengsoft.system.domain.Asset;

/**
 * The facade interface of {@link Contract}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ContractFacade extends EntityFacade<ContractService, Contract, String>, ContractService {

    /**
     * Save with pictures
     * 
     * @param contract {@link Contract}
     * @param pictures {@link Asset}
     */
    Contract saveWithPictures(@Valid Contract contract, List<Asset> pictures);

    /**
     * delete {@link ContractPicture} by given {@link Asset}
     * 
     * @param contract {@link Contract}
     * @param asset    {@link Asset}
     */
    Contract deletePictureByAsset(Contract contract, @NotNull Asset asset);

}
