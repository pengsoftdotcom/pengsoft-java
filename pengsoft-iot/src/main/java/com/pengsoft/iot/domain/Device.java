package com.pengsoft.iot.domain;

import java.time.LocalDateTime;
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
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.support.domain.Codeable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Device product
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class Device extends OwnedExtEntityImpl implements Codeable {

    private static final long serialVersionUID = -2430815767746162830L;

	@NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String ip;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Product product;

    private LocalDateTime connectedAt;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE)
    private List<GroupDevice> groupDevices = new ArrayList<>();

}
