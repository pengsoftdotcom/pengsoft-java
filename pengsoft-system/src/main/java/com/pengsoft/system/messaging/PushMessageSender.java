package com.pengsoft.system.messaging;

import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Message;

@Named
public class PushMessageSender implements MessageSender {

    public void send(@NotNull Message message) {
    }

}
