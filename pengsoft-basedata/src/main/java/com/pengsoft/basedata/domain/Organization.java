package com.pengsoft.basedata.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Organization
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "organization_name", columnList = "name", unique = true),
        @Index(name = "organization_short_name", columnList = "shortName") })
@Authorized
public class Organization extends OwnedExtTreeEntityImpl<Organization> {

    private static final long serialVersionUID = 5421711754682104714L;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset logo;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem type;

    @OneToOne(fetch = FetchType.LAZY)
    private Authentication authentication;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Person legalRepresentative;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private BusinessLicense businessLicense;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE)
    private List<Rank> ranks = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE)
    private List<Department> departments = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.REMOVE)
    private List<SupplierConsumer> suppliers = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "consumer", cascade = CascadeType.REMOVE)
    private List<SupplierConsumer> consumers = new ArrayList<>();

}
