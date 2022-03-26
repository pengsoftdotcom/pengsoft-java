package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.repository.InternalMessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link InternalMessageService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class InternalMessageServiceImpl extends EntityServiceImpl<InternalMessageRepository, InternalMessage, String>
        implements InternalMessageService {

}
