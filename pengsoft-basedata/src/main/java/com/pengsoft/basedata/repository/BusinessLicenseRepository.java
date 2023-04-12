package com.pengsoft.basedata.repository;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.QBusinessLicense;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link BusinessLicense} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface BusinessLicenseRepository
        extends EntityRepository<QBusinessLicense, BusinessLicense, String>, OwnedRepository {

}
