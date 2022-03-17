package com.pengsoft.oa.api;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.type.MapType;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.facade.ContractFacade;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.system.service.DictionaryTypeService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Contract}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/oa/contract")
public class ContractApi extends EntityApi<ContractFacade, Contract, String> {

    private static final String CONTRACT_PARTY_TYPE_ORGANIZATION = "organization";

    @Inject
    private DictionaryTypeService dictionaryTypeService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private PersonService personService;

    private ObjectMapper objectMapper;

    private MapType mapType;

    public ContractApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    }

    @Messaging(builder = "contractConfirmMessageBuilder")
    @PostMapping("save-with-pictures")
    public void saveWithPictures(@RequestBody Contract contract,
            @RequestParam(value = "picture.id", required = false) List<Asset> pictures) {
        getService().saveWithPictures(contract, pictures);
    }

    @Messaging(builder = "contractConfirmMessageBuilder")
    @DeleteMapping("delete-picture-by-asset")
    public void deletePictureByAsset(@RequestParam(value = "id", required = false) Contract contract,
            @RequestParam("asset.id") Asset asset) {
        getService().deletePictureByAsset(contract, asset);
    }

    @PutMapping("confirm")
    public void confirm(@RequestParam("id") Contract contract) {
        getService().confirm(contract);
    }

    @GetMapping("find-one-with-party")
    public Map<String, Object> findOneWithParty(@RequestParam(value = "id", required = false) Contract source) {
        final var target = super.findOne(source);
        if (StringUtils.isBlank(target.getId()) && !SecurityUtils.hasAnyRole(Role.ADMIN)) {
            dictionaryTypeService.findOneByCode("contract_party_type").ifPresent(type -> dictionaryItemService
                    .findOneByTypeAndParentAndCode(type, null, CONTRACT_PARTY_TYPE_ORGANIZATION).ifPresent(item -> {
                        target.setPartyAType(item);
                        target.setPartyAId(SecurityUtilsExt.getPrimaryOrganizationId());
                    }));

        }
        final var data = setParty(target);
        data.put("pictures", target.getPictures().stream().map(ContractPicture::getAsset).toList());
        return data;
    }

    @GetMapping("find-page-with-party")
    public Page<Map<String, Object>> findPageWithParty(final Predicate predicate, final Pageable pageable) {
        final var page = super.findPage(predicate, pageable);
        final var content = page.getContent().stream().map(this::setParty).toList();
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    private Map<String, Object> setParty(Contract contract) {
        Map<String, Object> data = objectMapper.convertValue(contract, mapType);
        final var partyAType = contract.getPartyAType();
        if (partyAType != null) {
            if (partyAType.getCode().equals(CONTRACT_PARTY_TYPE_ORGANIZATION)) {
                organizationService.findOne(contract.getPartyAId()).ifPresent(partyA -> data.put("partyA", partyA));
            } else {
                personService.findOne(contract.getPartyAId()).ifPresent(partyA -> data.put("partyA", partyA));
            }
        }

        final var partyBType = contract.getPartyBType();
        if (partyBType != null) {
            if (partyBType.getCode().equals(CONTRACT_PARTY_TYPE_ORGANIZATION)) {
                organizationService.findOne(contract.getPartyBId()).ifPresent(partyB -> data.put("partyB", partyB));
            } else {
                personService.findOne(contract.getPartyBId()).ifPresent(partyB -> data.put("partyB", partyB));
            }
        }
        return data;
    }

    @Override
    public Page<Contract> findPage(Predicate predicate, Pageable pageable) {
        Page<Contract> page = super.findPage(predicate, pageable);
        page.getContent().forEach(this::setParty);
        return page;
    }

}
