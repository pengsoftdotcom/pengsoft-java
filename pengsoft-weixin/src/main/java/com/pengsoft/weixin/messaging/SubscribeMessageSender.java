package com.pengsoft.weixin.messaging;

import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Message;
import com.pengsoft.system.messaging.MessageSender;

/**
 * Weixin miniapp subscribe message sender.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class SubscribeMessageSender implements MessageSender {

    @Override
    public void send(@NotNull Message message) {

    }

}
