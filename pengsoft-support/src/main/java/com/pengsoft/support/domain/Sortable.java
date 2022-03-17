package com.pengsoft.support.domain;

/**
 * Sortable
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Sortable extends Entity<String> {

    long getSequence();

    void setSequence(long sequence);

}
