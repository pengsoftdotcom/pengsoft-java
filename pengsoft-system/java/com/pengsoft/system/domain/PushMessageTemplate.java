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

/**
 * Message template
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class PushMessageTemplate extends MessageTemplate {

    @NotBlank
    @Size(max = 255)
    private String subject;

    @Override
    public Message toMessage(User sender, User receiver) {
        final var message = new PushMessage();
        BeanUtils.copyProperties(this, message);
        message.setSender(sender);
        message.setReceiver(receiver);
        return message;
    }

}
