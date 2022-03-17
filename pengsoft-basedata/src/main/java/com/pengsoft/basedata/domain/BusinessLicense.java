package com.pengsoft.basedata.domain;

import java.time.LocalDate;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.system.domain.Address;
import com.pengsoft.system.domain.Asset;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Business license
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class BusinessLicense extends OwnedEntityImpl {

    private static final long serialVersionUID = -3180695776694223095L;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset asset;

    @NotNull
    @Valid
    @AssociationOverride(name = "region", joinColumns = @JoinColumn(name = "address_region_id"))
    @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    private Address address;

    @NotBlank
    @Size(max = 255)
    private String business;

    @NotBlank
    @Size(max = 255)
    private String capital;

    @NotNull
    private LocalDate establishDate;

    @NotBlank
    @Size(max = 255)
    private String legalPerson;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String registerNumber;

    @NotBlank
    @Size(max = 255)
    private String type;

    @NotNull
    private LocalDate validPeriod;

}
