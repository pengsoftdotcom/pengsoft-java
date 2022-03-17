package com.pengsoft.system.messaging;

import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Message;

/**
 * Email message sender
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class InternalMessageSender implements MessageSender {

    @Override
    public void send(@NotNull Message message) {

    }

}
