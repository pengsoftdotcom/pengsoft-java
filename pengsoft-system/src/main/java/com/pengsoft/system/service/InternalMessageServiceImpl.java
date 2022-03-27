package com.pengsoft.system.service;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.repository.InternalMessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class InternalMessageServiceImpl extends EntityServiceImpl<InternalMessageRepository, InternalMessage, String>
        implements InternalMessageService {

    @Override
    public long countByReceiverAndReadAtIsNull(User receiver) {
        return getRepository().countByReceiverIdAndReadAtIsNull(receiver.getId());
    }

}
