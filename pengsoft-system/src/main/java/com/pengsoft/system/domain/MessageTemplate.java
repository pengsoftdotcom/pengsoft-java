package com.pengsoft.system.domain;

import java.time.LocalDateTime;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Message template
 */
@Getter
@Setter
@MappedSuperclass
public class MessageTemplate extends EntityImpl {

    @NotBlank
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    private LocalDateTime sentAt;

    public Message toMessage(User sender, User receiver) {
        throw new IllegalAccessError("not implemented");
    }

}
