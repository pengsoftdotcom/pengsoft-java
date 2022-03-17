package com.pengsoft.basedata.service;

import com.pengsoft.basedata.domain.Authentication;
import com.pengsoft.basedata.repository.AuthenticationRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link Authentication} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class AuthenticationServiceImpl extends EntityServiceImpl<AuthenticationRepository, Authentication, String>
        implements AuthenticationService {

}
