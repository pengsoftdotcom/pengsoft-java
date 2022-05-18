package com.pengsoft.oa.facade;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractConfirmPicture;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.service.ContractConfirmPictureService;
import com.pengsoft.oa.service.ContractPictureService;
import com.pengsoft.oa.service.ContractService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.DateUtils;
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

    private static final String CONTRACT_PARTY_TYPE = "contract_party_type";

    private static final String CONTRACT_STATUS_NOT_UPLOADED = "not_uploaded";

    private static final String CONTRACT_STATUS = "contract_status";

    @Inject
    private ContractPictureService contractPictureService;

    @Inject
    private ContractConfirmPictureService contractConfirmPictureService;

    @Inject
    private AssetService assetService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private StaffService staffService;

    @Override
    public void generate(@NotEmpty List<Department> departments, @NotEmpty List<String> roleCodes) {
        final var partyA = departments.get(0).getOrganization();
        final var partyAType = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode(CONTRACT_PARTY_TYPE, null, "organization").orElseThrow();
        final var partyBType = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode(CONTRACT_PARTY_TYPE, null, "personal").orElseThrow();
        final var status = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, CONTRACT_STATUS_NOT_UPLOADED)
                .orElseThrow();
        staffService.findAllByDepartmentsAndRoleCodes(departments, roleCodes).forEach(staff -> {
            final var contract = findOneByPartyAIdAndPartyBId(partyA.getId(), staff.getPerson().getId())
                    .orElse(new Contract());
            if (StringUtils.isBlank(contract.getId())) {
                contract.setStatus(status);
                contract.setPartyAId(partyA.getId());
                contract.setPartyAType(partyAType);
                contract.setPartyBId(staff.getPerson().getId());
                contract.setPartyBType(partyBType);
                contract.setBelongsTo(staff.getJob().getDepartment().getOrganization().getId());
                contract.setControlledBy(staff.getJob().getDepartment().getId());
                save(contract);
            }
        });
    }

    @Override
    public Contract saveWithPictures(Contract contract, List<Asset> pictures, List<Asset> confirmPictures) {
        if (StringUtils.isBlank(contract.getId())) {
            super.save(contract);
        }
        handleContractPictures(contract, pictures);
        handleContractConfirmPictures(contract, confirmPictures);
        handleContractStatus(contract);
        return super.save(contract);
    }

    private void handleContractStatus(Contract contract) {
        DictionaryItem status = null;
        if (CollectionUtils.isEmpty(contract.getPictures())) {
            status = dictionaryItemService
                    .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, CONTRACT_STATUS_NOT_UPLOADED)
                    .orElseThrow(
                            () -> getExceptions().entityNotExists(DictionaryItem.class, CONTRACT_STATUS_NOT_UPLOADED));
            contract.setConfirmedAt(null);
            contract.setConfirmedBy(null);
        } else {
            if (CollectionUtils.isEmpty(contract.getConfirmPictures())) {
                status = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, "unconfirmed")
                        .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, "unconfirmed"));
                contract.setConfirmedAt(null);
                contract.setConfirmedBy(null);
            } else {
                status = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode(CONTRACT_STATUS, null, "confirmed")
                        .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, "confirmed"));
                contract.setConfirmedAt(DateUtils.currentDateTime());
                contract.setConfirmedBy(SecurityUtils.getUserId());
            }
        }
        contract.setStatus(status);

    }

    private void handleContractConfirmPictures(Contract contract, List<Asset> pictures) {
        contract.setConfirmPictures(contractConfirmPictureService.findAllByContract(contract));
        final var source = contract.getConfirmPictures();
        final var target = pictures.stream().map(asset -> new ContractConfirmPicture(contract, asset)).toList();
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(s -> EntityUtils.equals(t.getAsset(), s.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(created)) {
            contractConfirmPictureService.save(created);
        }
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(t -> EntityUtils.equals(s.getAsset(), t.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(deleted)) {
            contractConfirmPictureService.delete(source);
            source.removeIf(picture -> deleted.stream().anyMatch(d -> EntityUtils.equals(picture, d)));
        }
        source.addAll(created);
    }

    private void handleContractPictures(Contract contract, List<Asset> pictures) {
        contract.setPictures(contractPictureService.findAllByContract(contract));
        final var source = contract.getPictures();
        final var target = pictures.stream().map(asset -> new ContractPicture(contract, asset)).toList();
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(s -> EntityUtils.equals(t.getAsset(), s.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(created)) {
            contractPictureService.save(created);
        }
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(t -> EntityUtils.equals(s.getAsset(), t.getAsset()))).toList();
        if (CollectionUtils.isNotEmpty(deleted)) {
            contractPictureService.delete(source);
            source.removeIf(picture -> deleted.stream().anyMatch(d -> EntityUtils.equals(picture, d)));
        }
        source.addAll(created);
    }

    @Override
    public void deletePictureByAsset(Contract contract, Asset target) {
        if (contract == null) {
            assetService.delete(target);
        } else {
            contract.getPictures().stream()
                    .filter(source -> EntityUtils.equals(source.getAsset(), target)).findFirst()
                    .ifPresentOrElse(picture -> {
                        contractPictureService.delete(picture);
                        contract.getPictures().remove(picture);
                        super.save(contract);
                    }, () -> assetService.delete(target));
        }
    }

    @Override
    public void deleteConfirmPictureByAsset(Contract contract, Asset target) {
        if (contract == null) {
            assetService.delete(target);
        } else {
            contract.getConfirmPictures().stream()
                    .filter(source -> EntityUtils.equals(source.getAsset(), target)).findFirst()
                    .ifPresentOrElse(picture -> {
                        contractConfirmPictureService.delete(picture);
                        contract.getConfirmPictures().remove(picture);
                        super.save(contract);
                    }, () -> assetService.delete(target));
        }
    }

    @Override
    public void delete(Contract contract) {
        contractPictureService.delete(contract.getPictures());
        contractConfirmPictureService.delete(contract.getConfirmPictures());
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

    @Override
    public Optional<Contract> findOneByPartyAIdAndPartyBId(@NotBlank String partyAId, @NotBlank String partyBId) {
        return getService().findOneByPartyAIdAndPartyBId(partyAId, partyBId);
    }

}