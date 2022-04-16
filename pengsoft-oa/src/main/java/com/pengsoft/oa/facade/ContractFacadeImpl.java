package com.pengsoft.oa.facade;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.service.ContractPictureService;
import com.pengsoft.oa.service.ContractService;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.DictionaryItemService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link ContractFacade}
 *
 * @author peng.dangs@pengsoft.com
 * @since 1.0.0
 */
@Service
public class ContractFacadeImpl extends EntityFacadeImpl<ContractService, Contract, String>
        implements ContractFacade {

    private static final String CONTRACT_STATUS = "contract_status";

    @Inject
    private ContractPictureService contractPictureService;

    @Inject
    private AssetService assetService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Override
    public Contract saveWithPictures(Contract contract, List<Asset> pictures) {
        if (contract.getConfirmedAt() != null) {
            throw new BusinessException("contract.confirm.already");
        }
        if (StringUtils.isBlank(contract.getId())) {
            super.save(contract);
        }
        contract.setPictures(contractPictureService.findAllByContract(contract));
        final var sourcePictures = contract.getPictures();
        final var targetPictures = pictures.stream().map(asset -> new ContractPicture(contract, asset)).toList();
        final var createdPictures = targetPictures.stream().filter(t -> sourcePictures.stream().noneMatch(
                s -> EntityUtils.equals(t.getAsset(), s.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(createdPictures)) {
            contractPictureService.save(createdPictures);
        }
        final var deletedPictures = sourcePictures.stream().filter(s -> targetPictures.stream().noneMatch(
                t -> EntityUtils.equals(s.getAsset(), t.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(deletedPictures)) {
            contractPictureService.delete(sourcePictures);
            sourcePictures.removeIf(
                    picture -> deletedPictures.stream().anyMatch(deleted -> EntityUtils.equals(picture, deleted)));
        }
        sourcePictures.addAll(createdPictures);

        var status = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, "confirmed")
                .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, "confirmed"));
        if (contract.getConfirmedAt() == null) {
            if (CollectionUtils.isNotEmpty(contract.getPictures())) {
                status = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, "unconfirmed")
                        .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, "unconfirmed"));
            } else {
                status = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, "not_uploaded")
                        .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, "not_uploaded"));
            }
        }
        contract.setStatus(status);
        return super.save(contract);
    }

    @Override
    public Contract deletePictureByAsset(Contract contract, Asset target) {
        if (contract == null) {
            assetService.delete(target);
        } else {
            final var picture = contract.getPictures().stream()
                    .filter(source -> EntityUtils.equals(source.getAsset(), target)).findFirst()
                    .orElseThrow(() -> getExceptions().entityNotExists(ContractPicture.class, target.getId()));
            contractPictureService.delete(picture);
            contract.getPictures().remove(picture);
            super.save(contract);
        }
        return contract;
    }

    @Override
    public void delete(Contract contract) {
        contractPictureService.delete(contract.getPictures());
        super.delete(contract);
    }

    @Override
    public void confirm(Contract contract) {
        getService().confirm(contract);
    }

    @Override
    public List<Map<String, Object>> statisticByDepartment(@NotEmpty List<Department> departments) {
        return getService().statisticByDepartment(departments);
    }

}