package com.pengsoft.oa.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.basedata.domain.Staff;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Payroll record
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class PayrollDetail extends OwnedExtEntityImpl {

    private static final long serialVersionUID = -4847027306280433452L;

	@NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private PayrollRecord payroll;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff staff;

    private BigDecimal grossPay;

    private BigDecimal netPay;

    private LocalDateTime confirmedAt;

    private String confirmedBy;

}
