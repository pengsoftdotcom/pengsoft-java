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
public class SubscribeMessageTemplate extends MessageTemplate {

    private static final long serialVersionUID = -1689461343766902151L;

	@Size(max = 255)
    @NotBlank
    private String appid;

    @Size(max = 255)
    @NotBlank
    private String templateId;

    @Size(max = 255)
    private String page;

    public Message toMessage(User sender, User receiver) {
        SubscribeMessage message = new SubscribeMessage();
        BeanUtils.copyProperties(this, message);
        message.setSender(sender);
        message.setReceiver(receiver);
        return message;
    }
}
