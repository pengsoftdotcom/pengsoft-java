package com.pengsoft.security.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.util.StringUtils;

/**
 * {@link EntityImpl} implements {@link Owned}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@MappedSuperclass
public class OwnedEntityImpl extends EntityImpl implements Owned {

    private static final long serialVersionUID = 1718332746243664650L;

    @Size(max = 255)
    @Column(updatable = false)
    private String createdBy;

    @Size(max = 255)
    @Column(updatable = false)
    private String updatedBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

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
        final var userId = SecurityUtils.getUserId();
        setUpdatedBy(userId);
    }

}
