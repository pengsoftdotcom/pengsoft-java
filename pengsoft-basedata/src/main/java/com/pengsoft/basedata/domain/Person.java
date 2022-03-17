package com.pengsoft.basedata.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.json.MobileJsonSerializer;
import com.pengsoft.support.validation.Chinese;
import com.pengsoft.support.validation.Mobile;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Person
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "person_mobile", columnList = "mobile", unique = true),
        @Index(name = "person_identity_card_number", columnList = "identityCardNumber", unique = true),
        @Index(name = "person_name", columnList = "name") })
@Authorized
public class Person extends OwnedEntityImpl {

    private static final long serialVersionUID = 2954762383113778002L;

    @NotBlank
    @Size(min = 2, max = 20)
    @Chinese
    private String name;

    @Size(max = 255)
    private String nickname;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem gender;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset avatar;

    @NotBlank
    @Mobile
    @JsonSerialize(using = MobileJsonSerializer.class)
    @Column(updatable = false)
    private String mobile;

    @OneToOne(fetch = FetchType.LAZY)
    private Authentication authentication;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    @Valid
    @OneToOne(cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    private IdentityCard identityCard;

    @Size(max = 255)
    @Column(insertable = false, updatable = false, unique = true)
    private String identityCardNumber;

}
