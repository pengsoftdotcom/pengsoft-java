package com.pengsoft.oa.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.type.MapType;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.domain.QContract;
import com.pengsoft.oa.facade.ContractFacade;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.annotation.Messaging;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.system.service.StorageService;
import com.pengsoft.task.annotation.TaskHandler;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * The web api of {@link Contract}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Constant.API_PREFIX + "/oa/contract")
public class ContractApi extends EntityApi<ContractFacade, Contract, String> {

    private static final String CONTRACT_PARTY_TYPE_ORGANIZATION = "organization";

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private PersonService personService;

    @Inject
    private AssetService assetService;

    @Inject
    private StorageService storageService;

    private ObjectMapper objectMapper;

    private MapType type;

    public ContractApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        type = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    }

    @TaskHandler(name = "contractConfirmTaskHandler", create = true)
    @Messaging(builder = "contractConfirmMessageBuilder")
    @PostMapping("save-with-pictures")
    public void saveWithPictures(@RequestBody Contract contract,
            @RequestParam(value = "picture.id", defaultValue = "") List<Asset> pictures) {
        getService().saveWithPictures(contract, pictures);
    }

    @DeleteMapping("delete-picture-by-asset")
    public void deletePictureByAsset(@RequestParam(value = "id", required = false) Contract contract,
            @RequestParam("asset.id") Asset asset) {
        getService().deletePictureByAsset(contract, asset);
    }

    @TaskHandler(name = "contractConfirmTaskHandler", finish = true)
    @PutMapping("confirm")
    public void confirm(@RequestParam("id") Contract contract) {
        getService().confirm(contract);
    }

    @GetMapping("find-one-with-party")
    public Map<String, Object> findOneWithParty(@RequestParam(value = "id", required = false) Contract source) {
        final var target = super.findOne(source);
        if (StringUtils.isBlank(target.getId()) && !SecurityUtils.hasAnyRole(Role.ADMIN)) {
            dictionaryItemService
                    .findOneByTypeCodeAndParentAndCode("contract_party_type", null, CONTRACT_PARTY_TYPE_ORGANIZATION)
                    .ifPresent(item -> {
                        target.setPartyAType(item);
                        target.setPartyAId(SecurityUtilsExt.getPrimaryOrganizationId());
                    });

        }
        final var data = setParty(target);
        data.put("pictures", target.getPictures().stream().map(ContractPicture::getAsset).map(asset -> {
            if (asset.isLocked()) {
                storageService.download(asset);
                try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ByteArrayInputStream is = new ByteArrayInputStream(asset.getData());) {
                    Thumbnails.of(is).outputFormat("jpg").size(600, 600).toOutputStream(os);
                    asset.setAccessPath(
                            "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(os.toByteArray()));
                } catch (Exception e) {
                    log.error("asset.download.failed: {}", e.getMessage());
                }
            }
            return asset;
        }).toList());
        return data;
    }

    @GetMapping("find-page-with-party")
    public Page<Map<String, Object>> findPageWithParty(final Predicate predicate, final Pageable pageable) {
        final var page = super.findPage(predicate, pageable);
        final var content = page.getContent().stream().map(this::setParty).toList();
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    private Map<String, Object> setParty(Contract contract) {
        Map<String, Object> data = objectMapper.convertValue(contract, type);
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

    @Authorized
    @GetMapping("find-one-of-mine")
    public Map<String, Object> findOneOfMine(String id) {
        return findOneWithParty(getService().findOne(id).orElse(null));
    }

    @Authorized
    @GetMapping("find-page-of-mine")
    public Page<Map<String, Object>> findPageOfMine(Predicate predicate, Pageable pageable) {
        predicate = QueryDslUtils.merge(predicate, QContract.contract.partyBId.eq(SecurityUtilsExt.getPersonId()));
        return findPageWithParty(predicate, pageable);
    }

    @TaskHandler(name = "contractConfirmTaskHandler", finish = true)
    @Authorized
    @PutMapping("confirm-mine")
    public void confirmMine(String id) {
        getService().findOne(id).ifPresent(getService()::confirm);
    }

    @Authorized
    @GetMapping("download")
    public String download(String id, @RequestParam(defaultValue = "600") int width,
            @RequestParam(defaultValue = "600") int height) {
        var asset = assetService.findOne(id).orElseThrow(() -> getExceptions().entityNotExists(Asset.class, id));
        asset = this.storageService.download(asset);
        if (asset.getContentType().startsWith("image")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ByteArrayInputStream is = new ByteArrayInputStream(asset.getData());) {
                Thumbnails.of(is).outputFormat("jpg").size(width, height)
                        .toOutputStream(os);
                return "data:image/jpeg;base64," + Base64Utils.encodeToString(os.toByteArray());
            } catch (Exception e) {
                throw new BusinessException("asset.download.failed", e.getMessage());
            }

        }
        return Base64Utils.encodeToString(asset.getData());
    }

    @GetMapping("statistic-by-department")
    public List<Map<String, Object>> statisticByDepartment(
            @RequestParam(value = "department.id", required = false) List<Department> departments) {
        return getService().statisticByDepartment(departments);
    }

}
