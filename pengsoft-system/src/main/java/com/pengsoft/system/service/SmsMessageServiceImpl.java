package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.SmsMessage;
import com.pengsoft.system.repository.SmsMessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SmsMessageService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SmsMessageServiceImpl extends EntityServiceImpl<SmsMessageRepository, SmsMessage, String>
        implements SmsMessageService {

}
