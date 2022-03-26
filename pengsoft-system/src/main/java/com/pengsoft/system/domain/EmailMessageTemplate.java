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
public class EmailMessageTemplate extends MessageTemplate {

    private static final long serialVersionUID = 62125479543810487L;
	@NotBlank
    @Size(max = 255)
    private String subject;

    public Message toMessage(User sender, User receiver) {
        EmailMessage message = new EmailMessage();
        BeanUtils.copyProperties(this, message);
        message.setSender(sender);
        message.setReceiver(receiver);
        return message;
    }

}
