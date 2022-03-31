package com.pengsoft.ss.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Construction project safety check
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class SafetyCheck extends EntityImpl implements Codeable {

    private static final long serialVersionUID = -1683778090254264112L;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private ConstructionProject project;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff checker;

    @Size(max = 255)
    @NotBlank
    private String code;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem type;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem status;

    @NotBlank
    @Size(max = 500)
    @Column(updatable = false)
    private String reason;

    @Column(updatable = false)
    private LocalDateTime submittedAt;

    private String result;

    private LocalDateTime handledAt;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "check", cascade = CascadeType.REMOVE)
    private List<SafetyCheckFile> files = new ArrayList<>();

}
