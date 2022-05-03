package com.pengsoft.oa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.basedata.domain.Staff;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * AttendanceRecord
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class AttendanceRecord extends OwnedExtEntityImpl {

    private static final long serialVersionUID = -2652386762637121804L;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff staff;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private AccessRecord enterRecord;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private AccessRecord exitRecord;

    private boolean illegal;

    private String remark;

    private int year;

    private int month;

    private int day;

}
