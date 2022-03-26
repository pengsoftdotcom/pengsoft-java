package com.pengsoft.oa.service;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.repository.ContractPictureRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link ContractPicture} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class ContractPictureServiceImpl extends EntityServiceImpl<ContractPictureRepository, ContractPicture, String>
        implements ContractPictureService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(ContractPicture contractPicture) {
        super.delete(contractPicture);
        assetService.delete(contractPicture.getAsset());
    }

    @Override
    public List<ContractPicture> findAllByContract(Contract contract) {
        return getRepository().findAllByContractId(contract.getId());
    }

}
