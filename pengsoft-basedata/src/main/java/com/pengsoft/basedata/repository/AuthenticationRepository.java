package com.pengsoft.basedata.repository;

import com.pengsoft.basedata.domain.Authentication;
import com.pengsoft.basedata.domain.QAuthentication;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link Authentication} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AuthenticationRepository extends EntityRepository<QAuthentication, Authentication, String> {

}
