package com.pengsoft.basedata.repository;

import com.pengsoft.basedata.domain.Authentication;
import com.pengsoft.basedata.domain.QAuthentication;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Authentication} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface AuthenticationRepository extends EntityRepository<QAuthentication, Authentication, String> {

}
