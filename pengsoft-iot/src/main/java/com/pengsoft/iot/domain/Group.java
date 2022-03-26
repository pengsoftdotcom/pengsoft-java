package com.pengsoft.iot.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.basedata.domain.OwnedExtTreeEntityImpl;
import com.pengsoft.support.domain.Codeable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Device group
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class Group extends OwnedExtTreeEntityImpl<Group> implements Codeable {

    private static final long serialVersionUID = -3016292017059199868L;

	@NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String remark;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<GroupDevice> groupDevices = new ArrayList<>();

    public Group(String id) {
        setId(id);
    }

}