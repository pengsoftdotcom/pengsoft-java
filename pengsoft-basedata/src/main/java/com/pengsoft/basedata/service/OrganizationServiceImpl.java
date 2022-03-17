package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.repository.OrganizationRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link OrganizationService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class OrganizationServiceImpl extends TreeEntityServiceImpl<OrganizationRepository, Organization, String>
        implements OrganizationService {

    @Inject
    private AssetService assetService;

    @Override
    public Organization save(final Organization organization) {
        getRepository().findOneByName(organization.getName()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, organization)) {
                throw getExceptions().constraintViolated("name", "exists", organization.getName());
            }
        });
        if (StringUtils.isBlank(organization.getShortName())) {
            organization.setShortName(organization.getName());
        }
        return super.save(organization);
    }

    @Override
    public void setLegalRepresentative(Organization organization, Person legalRepresentative) {
        getRepository().setLegalRepresentative(organization, legalRepresentative, SecurityUtils.getUserId());
    }

    @Override
    public void setBusinessLicense(Organization organization, BusinessLicense businessLicense) {
        getRepository().setBusinessLicense(organization, businessLicense, SecurityUtils.getUserId());
    }

    @Override
    public void delete(Organization organization) {
        super.delete(organization);
        if (organization.getLogo() != null) {
            assetService.delete(organization.getLogo());
        }
    }

    public long deleteLogoByAsset(Organization organization, Asset asset) {
        if (organization != null) {
            organization.setLogo(null);
            save(organization);
        }
        assetService.delete(asset);
        return organization == null ? -1 : organization.getVersion() + 1;
    }

    @Override
    public Optional<Organization> findOneByName(String name) {
        return getRepository().findOneByName(name);
    }

    @Override
    public Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable) {
        return getRepository().findPageOfAvailableConsumers(supplier, pageable);
    }

    @Override
    public List<Organization> findAllAvailableConsumers(Organization supplier) {
        return getRepository().findAllAvailableConsumers(supplier);
    }

    @Override
    public Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable) {
        return getRepository().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @Override
    public List<Organization> findAllAvailableSuppliers(Organization consumer) {
        return getRepository().findAllAvailableSuppliers(consumer);
    }

}
