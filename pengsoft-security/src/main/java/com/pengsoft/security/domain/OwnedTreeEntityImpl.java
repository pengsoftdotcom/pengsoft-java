package com.pengsoft.security.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.TreeEntityImpl;
import com.pengsoft.support.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link TreeEntityImpl} implements {@link Owned}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class OwnedTreeEntityImpl<T extends OwnedTreeEntityImpl<T>> extends TreeEntityImpl<T> implements Owned {

    private static final long serialVersionUID = 6277558798346028175L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 255)
    @Column(updatable = false)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 255)
    private String updatedBy;

    @Override
    public void preCreate() {
        super.preCreate();
        final var userId = SecurityUtils.getUserId();
        if (StringUtils.isBlank(getCreatedBy())) {
            setCreatedBy(userId);
        }
        if (StringUtils.isBlank(getUpdatedBy())) {
            setUpdatedBy(userId);
        }
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        setUpdatedBy(SecurityUtils.getUserId());
    }

}
