package com.pengsoft.support.service;

import java.util.List;

import com.pengsoft.support.domain.Sortable;

/**
 * The sort service interface.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SortService {

    <T extends Sortable> void sort(T entities);

    default <T extends Sortable> void sort(List<T> entities) {
        entities.forEach(this::sort);
    }

}
