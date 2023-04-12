package com.pengsoft.basedata.repository;

import com.pengsoft.basedata.domain.AuthenticationRecord;
import com.pengsoft.basedata.domain.QAuthenticationRecord;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link AuthenticationRecord} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AuthenticationRecordRepository
        extends EntityRepository<QAuthenticationRecord, AuthenticationRecord, String>, OwnedRepository {

}
