package com.pengsoft.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.util.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Authority
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "authority_code", columnList = "code", unique = true),
        @Index(name = "authority_name", columnList = "name", unique = true) })
public class Authority extends EntityImpl implements Codeable {

    private static final long serialVersionUID = 7662282600857868765L;

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
    @OneToMany(mappedBy = "authority", cascade = CascadeType.REMOVE)
    private List<RoleAuthority> roleAuthorities = new ArrayList<>();

    public Authority(@NotBlank @Size(max = 255) final String code) {
        this.code = code;
        this.name = StringUtils.replace(code, StringUtils.GLOBAL_SEPARATOR, StringUtils.SPACE);
    }

}
