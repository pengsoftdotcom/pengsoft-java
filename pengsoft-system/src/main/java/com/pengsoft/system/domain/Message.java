package com.pengsoft.system.domain;

import java.util.Map;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.User;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Message extends MessageTemplate {

    private static final long serialVersionUID = 6713219233836788064L;

	@NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User sender;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User receiver;

    @Transient
    private transient MessageTemplate template;

    @Transient
    private transient Map<String, String> params;

}
