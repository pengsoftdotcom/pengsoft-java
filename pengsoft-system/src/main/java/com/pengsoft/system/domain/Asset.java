package com.pengsoft.system.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pengsoft.security.domain.OwnedEntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = {
        @Index(name = "asset_created_by", columnList = "createdBy, originalName, contentType, contentLength, createdAt, updatedAt") })
public class Asset extends OwnedEntityImpl {

    private static final long serialVersionUID = -6128119816263577626L;

    @Size(max = 255)
    private String originalName;

    @Size(max = 255)
    private String presentName;

    @Size(max = 255)
    private String contentType;

    @Size(max = 255)
    private String storagePath;

    @Size(max = 255)
    private String accessPath;

    private long contentLength;

    private boolean locked;

    @JsonProperty
    @Transient
    private transient byte[] data;

}
