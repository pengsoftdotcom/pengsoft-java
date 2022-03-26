package com.pengsoft.system.messaging;

import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Message;

/**
 * The message sender.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface MessageSender {

    void send(@NotNull Message message);

}
