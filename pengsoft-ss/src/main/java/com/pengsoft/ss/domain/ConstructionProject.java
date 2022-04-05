package com.pengsoft.ss.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Construction project
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class ConstructionProject extends EntityImpl implements Codeable {

    private static final long serialVersionUID = 3844774310596073097L;

    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Min(1)
    @Max(28)
    private int payday = 20;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization regulatoryUnit;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff ruManager;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization owner;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff ownerManager;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization supervisionUnit;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff suManager;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization buildingUnit;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff buManager;

}
