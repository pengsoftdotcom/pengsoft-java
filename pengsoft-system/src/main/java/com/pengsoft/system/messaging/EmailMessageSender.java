package com.pengsoft.system.messaging;

import javax.inject.Named;

import com.pengsoft.system.domain.Message;

@Named
public class EmailMessageSender implements MessageSender {

    public void send(Message message) {
    }

}
