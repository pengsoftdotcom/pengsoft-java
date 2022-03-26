package com.pengsoft.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.Owned;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * Internal message
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class SmsMessage extends Message implements Owned {

    public static final String TYPE = "sms";

    @Size(max = 255)
    @Column(updatable = false)
    private String createdBy;

    @Size(max = 255)
    @Column(updatable = false)
    private String updatedBy;

    @Transient
    private transient SmsMessageTemplate template;

}
