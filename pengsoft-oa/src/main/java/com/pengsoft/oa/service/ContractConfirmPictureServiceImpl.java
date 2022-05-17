package com.pengsoft.oa.service;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractConfirmPicture;
import com.pengsoft.oa.repository.ContractConfirmPictureRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link ContractConfirmPicture} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class ContractConfirmPictureServiceImpl
        extends EntityServiceImpl<ContractConfirmPictureRepository, ContractConfirmPicture, String>
        implements ContractConfirmPictureService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(ContractConfirmPicture ContractConfirmPicture) {
        super.delete(ContractConfirmPicture);
        assetService.delete(ContractConfirmPicture.getAsset());
    }

    @Override
    public List<ContractConfirmPicture> findAllByContract(Contract contract) {
        return getRepository().findAllByContractId(contract.getId());
    }

}
