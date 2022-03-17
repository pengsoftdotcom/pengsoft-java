package com.pengsoft.support.domain;

/**
 * Enable
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Enable extends Entity<String> {

    boolean isEnabled();

    void setEnabled(boolean enabled);

}
