package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.repository.PushMessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PushMessageService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PushMessageServiceImpl extends EntityServiceImpl<PushMessageRepository, PushMessage, String>
        implements PushMessageService {

}
