package com.pengsoft.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.Owned;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class SmsMessage extends Message implements Owned {

    private static final long serialVersionUID = -8980502176560702174L;

	public static final String TYPE = "sms";

    @Size(max = 255)
    @Column(updatable = false)
    private String createdBy;

    @Size(max = 255)
    @Column(updatable = false)
    private String updatedBy;

}
