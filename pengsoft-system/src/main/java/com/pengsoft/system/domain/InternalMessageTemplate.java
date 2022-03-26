package com.pengsoft.system.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class InternalMessageTemplate extends MessageTemplate {

    private static final long serialVersionUID = -619203688852173628L;

    @NotBlank
    @Size(max = 255)
    private String subject;

    @Override
    public Message toMessage(User sender, User receiver) {
        InternalMessage message = new InternalMessage();
        BeanUtils.copyProperties(this, message);
        message.setSender(sender);
        message.setReceiver(receiver);
        return message;
    }

}
