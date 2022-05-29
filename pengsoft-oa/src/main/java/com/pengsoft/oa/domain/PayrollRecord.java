package com.pengsoft.oa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;

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
@Table(indexes = { @Index(name = "payroll_record_belongs_to", columnList = "belongsTo, year, month", unique = true) })
public class PayrollRecord extends OwnedExtEntityImpl {

    private static final long serialVersionUID = -1350196681980716549L;

    @Min(2022)
    private int year;

    @Min(1)
    @Max(12)
    private int month;

    private long paidCount;

    private long confirmedCount;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem status;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset sheet;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset signedSheet;

    private LocalDateTime importedAt;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "payroll", cascade = CascadeType.REMOVE)
    private List<PayrollDetail> details = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "payroll", cascade = CascadeType.REMOVE)
    private List<PayrollRecordConfirmPicture> confirmPictures = new ArrayList<>();

    public PayrollRecord() {
        final var now = DateUtils.currentDate();
        setYear(now.getYear());
        setMonth(now.getMonthValue());
    }

}
