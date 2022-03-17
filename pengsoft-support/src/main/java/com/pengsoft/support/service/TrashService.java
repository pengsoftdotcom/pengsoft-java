package com.pengsoft.support.service;

import java.util.List;

import com.pengsoft.support.domain.Trashable;

/**
 * The trash service interface.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface TrashService {

    <T extends Trashable> T trash(T entity);

    default <T extends Trashable> List<T> trash(final List<T> entities) {
        return entities.stream().map(this::trash).toList();
    }

    <T extends Trashable> T restore(T entity);

    default <T extends Trashable> List<T> restore(final List<T> entities) {
        return entities.stream().map(this::restore).toList();
    }

}
