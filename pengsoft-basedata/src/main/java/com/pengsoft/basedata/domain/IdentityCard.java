package com.pengsoft.basedata.domain;

import java.time.LocalDate;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.support.validation.IdentityNumber;
import com.pengsoft.system.domain.Address;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Identity card
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@NoArgsConstructor
public class IdentityCard extends OwnedEntityImpl {

    private static final long serialVersionUID = 4637581323295182862L;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset face;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset emblem;

    @Size(max = 255)
    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem gender;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem nationality;

    @NotNull
    private LocalDate birthday;

    @NotNull
    @Valid
    @AssociationOverride(name = "region", joinColumns = @JoinColumn(name = "address_region_id"))
    @AttributeOverride(name = "detail", column = @Column(name = "address_detail"))
    private Address address;

    @IdentityNumber
    @Column(unique = true)
    private String number;

    @Size(max = 255)
    @NotBlank
    private String issue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
