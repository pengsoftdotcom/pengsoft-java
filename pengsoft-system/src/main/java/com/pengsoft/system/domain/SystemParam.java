package com.pengsoft.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.domain.Param;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统参数
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SystemParam extends EntityImpl implements Codeable {

    private static final long serialVersionUID = -1546350489173233397L;

	@Size(max = 255)
    @NotBlank
    private String code;

    @Size(max = 255)
    @NotBlank
    private String name;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Param param;

}