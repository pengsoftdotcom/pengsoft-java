package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.SubscribeMessage;
import com.pengsoft.system.repository.SubscribeMessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class SubscribeMessageServiceImpl extends EntityServiceImpl<SubscribeMessageRepository, SubscribeMessage, String>
        implements SubscribeMessageService {
}
