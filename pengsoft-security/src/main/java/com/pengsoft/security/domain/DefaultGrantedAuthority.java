package com.pengsoft.security.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * The default implementer of {@link GrantedAuthority}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class DefaultGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -4634265789356521159L;

    private final Authority authority;

    public DefaultGrantedAuthority(final Authority authority) {
        Assert.isTrue(authority != null && StringUtils.isNotBlank(authority.getCode()), "authority must not be null");
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority.getCode();
    }

    @Override
    public int hashCode() {
        return this.getAuthority().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrantedAuthority grantedAuthority) {
            return StringUtils.equals(grantedAuthority.getAuthority(), getAuthority());
        } else {
            return super.equals(obj);
        }
    }

}
