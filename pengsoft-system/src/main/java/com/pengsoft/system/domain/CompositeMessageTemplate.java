package com.pengsoft.system.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "composite_message_template_code", columnList = "code", unique = true),
        @Index(name = "composite_message_template_name", columnList = "name", unique = true) })
@Authorized
public class CompositeMessageTemplate extends EntityImpl implements Codeable {

    private static final long serialVersionUID = -2644342408300823179L;

	@NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Valid
    @OneToOne(cascade = { CascadeType.ALL })
    @NotFound(action = NotFoundAction.IGNORE)
    private InternalMessageTemplate internal;

    @Valid
    @OneToOne(cascade = { CascadeType.ALL })
    @NotFound(action = NotFoundAction.IGNORE)
    private EmailMessageTemplate email;

    @Valid
    @OneToOne(cascade = { CascadeType.ALL })
    @NotFound(action = NotFoundAction.IGNORE)
    private SmsMessageTemplate sms;

    @Valid
    @OneToOne(cascade = { CascadeType.ALL })
    @NotFound(action = NotFoundAction.IGNORE)
    private PushMessageTemplate push;

    @Valid
    @OneToOne(cascade = { CascadeType.ALL })
    @NotFound(action = NotFoundAction.IGNORE)
    private SubscribeMessageTemplate subscribe;

}
