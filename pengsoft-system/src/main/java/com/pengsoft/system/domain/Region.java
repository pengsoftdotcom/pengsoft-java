package com.pengsoft.system.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.TreeEntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "region_code", columnList = "code", unique = true),
        @Index(name = "region_name", columnList = "name"), @Index(name = "region_index", columnList = "index") })
public class Region extends TreeEntityImpl<Region> implements Codeable {

    private static final long serialVersionUID = -4857562517173822239L;

    @NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    @Size(max = 255)
    private String index;

    @Size(max = 255)
    private String remark;

}
