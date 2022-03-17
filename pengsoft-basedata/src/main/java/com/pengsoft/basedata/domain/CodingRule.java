package com.pengsoft.basedata.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * Coding rule
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "coding_rule_entity", columnList = "entity", unique = true) })
public class CodingRule extends OwnedExtEntityImpl {

    @NotBlank
    @Size(max = 255)
    private String entity;

    @Size(max = 255)
    private String prefix;

    @Size(max = 255)
    private String suffix;

    @Min(1)
    private int step = 1;

    @Min(1)
    private int length = 1;

    @Min(1)
    private int index = 1;

    @Size(max = 255)
    private String value;

    @NotBlank
    @Size(max = 255)
    private String generator;

}
