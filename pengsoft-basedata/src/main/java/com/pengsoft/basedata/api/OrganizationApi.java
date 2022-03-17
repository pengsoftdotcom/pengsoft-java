package com.pengsoft.basedata.api;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.facade.OrganizationFacade;
import com.pengsoft.basedata.facade.PersonFacadeImpl;
import com.pengsoft.basedata.service.BusinessLicenseServiceImpl;
import com.pengsoft.basedata.service.RecognitionService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.TreeEntityApi;
import com.pengsoft.system.domain.Asset;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Organization}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/organization")
public class OrganizationApi extends TreeEntityApi<OrganizationFacade, Organization, String> {

    @Inject
    private PersonFacadeImpl personfacade;

    @Inject
    private BusinessLicenseServiceImpl businessLicenseService;

    private RecognitionService recognitionService;

    public OrganizationApi(ApplicationContext context) {
        if (context.containsBean("recognitionService")) {
            this.recognitionService = context.getBean(RecognitionService.class);
        }
    }

    @PostMapping("submit")
    public void submit(@RequestBody Organization organization) {
        getService().submit(organization);
    }

    @PostMapping("authenticate")
    public void authenticate(@RequestParam("id") Organization organization, boolean authenticated) {
        getService().authenticate(organization, authenticated);
    }

    @PostMapping("set-legal-representative")
    public void setLegalRepresentative(@RequestParam("id") Organization organization,
            @RequestBody(required = false) Person legalRepresentative) {
        if (legalRepresentative != null) {
            personfacade.save(legalRepresentative);
            personfacade.flush();
        }
        getService().setLegalRepresentative(organization, legalRepresentative);
    }

    @PostMapping("set-business-license")
    public void setBusinessLicense(@RequestParam("id") Organization organization,
            @RequestBody(required = false) BusinessLicense businessLicense) {
        if (businessLicense != null) {
            businessLicenseService.save(businessLicense);
            businessLicenseService.flush();
        }
        getService().setBusinessLicense(organization, businessLicense);
    }

    @PostMapping("recognize-business-license")
    public BusinessLicense recognizeBusinessLicense(@RequestParam("asset.id") Asset asset) {
        return recognitionService.businessLicense(asset);
    }

    @DeleteMapping("delete-logo-by-asset")
    public long deleteLogoByAsset(@RequestParam(value = "id", required = false) Organization organization,
            @RequestParam("asset.id") Asset asset) {
        return getService().deleteLogoByAsset(organization, asset);
    }

    @GetMapping("get-legal-representative")
    public Person getLegalRepresentative(@RequestParam("id") Organization organization) {
        return Optional.ofNullable(organization.getLegalRepresentative()).orElse(new Person());
    }

    @GetMapping("get-business-license")
    public BusinessLicense getBusinessLicense(@RequestParam("id") Organization organization) {
        return Optional.ofNullable(organization.getBusinessLicense()).orElse(new BusinessLicense());
    }

    @GetMapping("find-page-of-available-consumers")
    public Page<Organization> findPageOfAvailableConsumers(@RequestParam("supplier.id") Organization supplier,
            Pageable pageable) {
        return getService().findPageOfAvailableConsumers(supplier, pageable);
    }

    @GetMapping("find-all-available-consumers")
    public List<Organization> findAllAvailableConsumers(@RequestParam("supplier.id") Organization supplier) {
        return getService().findAllAvailableConsumers(supplier);
    }

    @GetMapping("find-page-of-available-suppliers")
    public Page<Organization> findPageOfAvailableSuppliers(@RequestParam("consumer.id") Organization consumer,
            Pageable pageable) {
        return getService().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @GetMapping("find-all-available-suppliers")
    public List<Organization> findAllAvailableSuppliers(@RequestParam("consumer.id") Organization consumer) {
        return getService().findAllAvailableSuppliers(consumer);
    }

}
