package com.pengsoft.support.service;

import java.util.List;

import com.pengsoft.support.domain.Enable;

/**
 * The enable service interface.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface EnableService {

    <T extends Enable> void enable(T entity);

    /**
     * Set the value of the all entities' 'enabled' field to true.
     *
     * @param entities A collection of {@link Enable}.
     */
    default <T extends Enable> void enable(final List<T> entities) {
        entities.forEach(this::enable);
    }

    /**
     * Set the value of the entity's 'enabled' field to false.
     *
     * @param entity {@link Enable}
     */
    <T extends Enable> void disable(T entity);

    /**
     * Set the value of the all entities' 'enabled' field to true.
     *
     * @param entities A collection of {@link Enable}.
     */
    default <T extends Enable> void disable(final List<T> entities) {
        entities.forEach(this::disable);
    }

}
