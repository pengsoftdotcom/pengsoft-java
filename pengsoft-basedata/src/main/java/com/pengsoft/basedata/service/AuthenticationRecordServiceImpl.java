package com.pengsoft.basedata.service;

import com.pengsoft.basedata.domain.AuthenticationRecord;
import com.pengsoft.basedata.repository.AuthenticationRecordRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link AuthenticationRecord} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class AuthenticationRecordServiceImpl extends EntityServiceImpl<AuthenticationRecordRepository, AuthenticationRecord, String>
        implements AuthenticationRecordService {

}
