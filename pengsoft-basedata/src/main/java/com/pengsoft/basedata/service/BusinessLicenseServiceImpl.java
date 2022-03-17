package com.pengsoft.basedata.service;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.repository.BusinessLicenseRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link BusinessLicense} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class BusinessLicenseServiceImpl extends EntityServiceImpl<BusinessLicenseRepository, BusinessLicense, String>
        implements BusinessLicenseService {

    @Inject
    private AssetService assetService;

    @Override
    public void delete(BusinessLicense businessLicense) {
        super.delete(businessLicense);
        assetService.delete(businessLicense.getAsset());
    }

}
