package com.pengsoft.oa.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.system.domain.Asset;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Payroll
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class PayrollRecord extends OwnedExtEntityImpl implements Codeable {

    @NotBlank
    @Size(max = 255)
    private String code;

    private int paidCount;

    private int confirmedCount;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset sheet;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset signedSheet;

}
