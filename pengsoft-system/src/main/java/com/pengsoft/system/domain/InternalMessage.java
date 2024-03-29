package com.pengsoft.system.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.Owned;
import com.pengsoft.support.domain.Trashable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class InternalMessage extends Message implements Owned, Trashable {

    private static final long serialVersionUID = -5824473836182124339L;

    public static final String TYPE = "internal";

    @NotBlank
    @Size(max = 255)
    private String subject;

    @Size(max = 255)
    @Column(updatable = false)
    private String createdBy;

    @Size(max = 255)
    @Column(updatable = false)
    private String updatedBy;

    private LocalDateTime readAt;

    private LocalDateTime trashedAt;

}
