package com.pengsoft.system.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.InternalMessage;

public interface InternalMessageService extends EntityService<InternalMessage, String> {

    /**
     * 返回收件人未读消息数
     * 
     * @param receiver 收件人
     */
    long countByReceiverAndReadAtIsNull(@NotNull User receiver);

}
