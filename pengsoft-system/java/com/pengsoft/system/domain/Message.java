package com.pengsoft.system.domain;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.security.domain.User;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link OwnedEntityImpl} implements {@link Messageable}
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Message extends MessageTemplate {

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User sender;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User receiver;

}