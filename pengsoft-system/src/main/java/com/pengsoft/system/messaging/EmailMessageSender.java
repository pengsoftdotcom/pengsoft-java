package com.pengsoft.system.messaging;

import javax.inject.Named;

import com.pengsoft.system.domain.Message;

@Named
public class EmailMessageSender implements MessageSender {

    @Override
    public void send(Message message) {
    }

}
