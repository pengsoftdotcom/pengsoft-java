package com.pengsoft.basedata.facade;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Authentication;
import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.AuthenticationService;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.facade.TreeEntityFacadeImpl;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.system.service.DictionaryTypeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link OrganizationFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class OrganizationFacadeImpl extends TreeEntityFacadeImpl<OrganizationService, Organization, String>
        implements OrganizationFacade {

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private DictionaryTypeService dictionaryTypeService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Override
    public void submit(Organization organization) {
        save(organization);
        setLegalRepresentative(organization, organization.getLegalRepresentative());
        setBusinessLicense(organization, organization.getBusinessLicense());
        final var authentication = new Authentication();
        authentication.setName(organization.getName());
        authentication.setNumber(organization.getBusinessLicense().getRegisterNumber());
        dictionaryTypeService.findOneByCode("authentication_status").ifPresent(type -> dictionaryItemService
                .findOneByTypeAndParentAndCode(type, null, "submitted").ifPresent(authentication::setStatus));
        authenticationService.save(authentication);
        organization.setAuthentication(authentication);
        save(organization);
    }

    @Override
    public void authenticate(Organization organization, boolean authenticated) {
        final var authentication = organization.getAuthentication();
        if (StringUtils.notEquals(authentication.getStatus().getCode(), "submitted")) {
            throw new BusinessException("authentication.status.error", HttpStatus.BAD_REQUEST,
                    authentication.getStatus().getCode());
        }
        dictionaryTypeService.findOneByCode("authentication_status")
                .ifPresent(type -> dictionaryItemService
                        .findOneByTypeAndParentAndCode(type, null, authenticated ? "authenticated" : "unauthenticated")
                        .ifPresent(authentication::setStatus));
        authenticationService.save(authentication);
        save(organization);
    }

    @Override
    public void setLegalRepresentative(Organization organization, Person legalRepresentative) {
        getService().setLegalRepresentative(organization, legalRepresentative);
    }

    @Override
    public void setBusinessLicense(Organization organization, BusinessLicense businessLicense) {
        getService().setBusinessLicense(organization, businessLicense);
    }

    @Override
    public long deleteLogoByAsset(Organization organization, Asset asset) {
        return getService().deleteLogoByAsset(organization, asset);
    }

    @Override
    public Optional<Organization> findOneByName(String name) {
        return getService().findOneByName(name);
    }

    @Override
    public Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable) {
        return getService().findPageOfAvailableConsumers(supplier, pageable);
    }

    @Override
    public List<Organization> findAllAvailableConsumers(Organization supplier) {
        return getService().findAllAvailableConsumers(supplier);
    }

    @Override
    public Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable) {
        return getService().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @Override
    public List<Organization> findAllAvailableSuppliers(Organization consumer) {
        return getService().findAllAvailableSuppliers(consumer);
    }

}
