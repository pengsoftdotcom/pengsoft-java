package com.pengsoft.support.domain;

import java.time.LocalDateTime;

/**
 * Recyclable
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Trashable extends Entity<String> {

    LocalDateTime getTrashedAt();

    void setTrashedAt(LocalDateTime trashedAt);

}
