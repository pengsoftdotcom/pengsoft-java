package com.pengsoft.basedata.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.OwnedTreeEntityImpl;
import com.pengsoft.support.util.StringUtils;

/**
 * {@link OwnedTreeEntity} implements {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@MappedSuperclass
public class OwnedExtTreeEntityImpl<T extends OwnedTreeEntityImpl<T>> extends OwnedTreeEntityImpl<T>
        implements OwnedExt {

    private static final long serialVersionUID = 7727144401744150942L;

    @Size(max = 255)
    @Column(updatable = false)
    private String controlledBy;

    @Size(max = 255)
    @Column(updatable = false)
    private String belongsTo;

    public String getControlledBy() {
        return controlledBy;
    }

    public void setControlledBy(String controlledBy) {
        this.controlledBy = controlledBy;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    @Override
    public void preCreate() {
        super.preCreate();
        if (StringUtils.isBlank(getControlledBy())) {
            setControlledBy(SecurityUtilsExt.getPrimaryDepartmentId());
        }
        if (StringUtils.isBlank(getBelongsTo())) {
            setBelongsTo(SecurityUtilsExt.getPrimaryOrganizationId());
        }
    }

}
