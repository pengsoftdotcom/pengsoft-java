package com.pengsoft.system.messaging;

import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Message;

public interface MessageSender {

    void send(@NotNull Message paramMessage);

}
