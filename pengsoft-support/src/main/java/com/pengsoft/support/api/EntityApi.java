package com.pengsoft.support.api;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.support.domain.Enable;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.domain.Sortable;
import com.pengsoft.support.domain.Trashable;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.service.EnableService;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.support.service.SortService;
import com.pengsoft.support.service.TrashService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * The web api of {@link Entity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Validated
public class EntityApi<S extends EntityService<T, ID>, T extends Entity<ID>, ID extends Serializable> {

    @Getter
    @Inject
    private Exceptions exceptions;

    @Getter
    @Inject
    private S service;

    @Inject
    private EnableService enableService;

    @Inject
    private TrashService trashService;

    @Inject
    private SortService sortService;

    @PostMapping("save")
    public void save(@RequestBody final T entity) {
        service.save(entity);
    }

    @DeleteMapping("delete")
    public void delete(final Predicate predicate) {
        List<T> entities = service.findAll(predicate, Sort.unsorted());
        service.delete(entities);
    }

    @SuppressWarnings("unchecked")
    @PutMapping("enable")
    public void enable(@RequestParam("id") final List<T> entities) {
        final var entityClass = service.getEntityClass();
        Assert.isTrue(Enable.class.isAssignableFrom(entityClass), entityClass.getName() + " is not a Enable");
        enableService.enable((List<Enable>) entities);
    }

    @SuppressWarnings("unchecked")
    @PutMapping("disable")
    public void disable(@RequestParam("id") final List<T> entities) {
        final var entityClass = service.getEntityClass();
        Assert.isTrue(Enable.class.isAssignableFrom(entityClass), entityClass.getName() + " is not a Enable");
        enableService.disable((List<Enable>) entities);
    }

    @SuppressWarnings("unchecked")
    @PutMapping("trash")
    public void trash(@RequestParam("id") final List<T> entities) {
        final var entityClass = service.getEntityClass();
        Assert.isTrue(Trashable.class.isAssignableFrom(entityClass), entityClass.getName() + " is not a Trashable");
        trashService.trash((List<Trashable>) entities);
    }

    @SuppressWarnings("unchecked")
    @PutMapping("restore")
    public void restore(@RequestParam("id") final List<T> entities) {
        final var entityClass = service.getEntityClass();
        Assert.isTrue(Trashable.class.isAssignableFrom(entityClass), entityClass.getName() + " is not a Trashable");
        trashService.restore((List<Trashable>) entities);
    }

    @SuppressWarnings("unchecked")
    @PutMapping("sort")
    public void sort(@RequestBody final List<T> entities) {
        final var entityClass = service.getEntityClass();
        Assert.isTrue(Sortable.class.isAssignableFrom(entityClass), entityClass.getName() + " is not a Sortable");
        sortService.sort((List<Sortable>) entities);
    }

    @GetMapping("find-one")
    @SneakyThrows
    public T findOne(@RequestParam(value = "id", required = false) final T entity) {
        return Optional.ofNullable(entity).orElse(service.getEntityClass().getDeclaredConstructor().newInstance());
    }

    @GetMapping("find-page")
    public Page<T> findPage(final Predicate predicate, final Pageable pageable) {
        return service.findPage(predicate, pageable);
    }

    @GetMapping("find-all")
    public List<T> findAll(final Predicate predicate, final Sort sort) {
        return service.findAll(predicate, sort);
    }

}
