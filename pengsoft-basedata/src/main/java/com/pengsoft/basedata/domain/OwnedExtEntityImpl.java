package com.pengsoft.basedata.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.support.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link OwnedEntity} implements {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public class OwnedExtEntityImpl extends OwnedEntityImpl implements OwnedExt {

    private static final long serialVersionUID = 1687056855769335888L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 255)
    @Column(updatable = false)
    private String controlledBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 255)
    @Column(updatable = false)
    private String belongsTo;

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
