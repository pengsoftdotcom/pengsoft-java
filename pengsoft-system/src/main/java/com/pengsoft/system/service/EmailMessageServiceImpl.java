package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.EmailMessage;
import com.pengsoft.system.repository.EmailMessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link EmailMessageService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class EmailMessageServiceImpl extends EntityServiceImpl<EmailMessageRepository, EmailMessage, String>
        implements EmailMessageService {

}
