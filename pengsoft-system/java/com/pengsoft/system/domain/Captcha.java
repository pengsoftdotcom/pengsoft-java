package com.pengsoft.system.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Captcha
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class Captcha extends EntityImpl implements Codeable {

    private static final long serialVersionUID = -1597178694701470008L;

    @Size(max = 255)
    private String code;

    @NotNull
    private LocalDateTime expiredAt;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

}
