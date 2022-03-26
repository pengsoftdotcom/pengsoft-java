package com.pengsoft.basedata.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.security.annotation.Authorized;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Department
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Authorized
public class Department extends OwnedExtTreeEntityImpl<Department> {

    private static final long serialVersionUID = 2966045982167958343L;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Organization organization;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private final List<Job> jobs = new ArrayList<>();

}
