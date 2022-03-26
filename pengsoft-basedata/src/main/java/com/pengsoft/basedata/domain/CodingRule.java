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
@Table(indexes = { @Index(name = "coding_rule_entity", columnList = "entity, controlledBy, belongsTo", unique = true) })
public class CodingRule extends OwnedExtEntityImpl {

    private static final long serialVersionUID = 362440273163308625L;

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

    @Min(0)
    private int index;

    @Size(max = 255)
    private String value;

    @NotBlank
    @Size(max = 255)
    private String generator;

}
