package com.pengsoft.system.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.support.repository.TreeEntityRepository;
import com.pengsoft.system.domain.QRegion;
import com.pengsoft.system.domain.Region;
import com.querydsl.core.types.dsl.StringExpression;

public interface RegionRepository extends TreeEntityRepository<QRegion, Region, String> {

    @Override
    default void customize(QuerydslBindings bindings, QRegion root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringExpression::startsWith);
        bindings.bind(root.name).first(StringExpression::startsWith);
    }

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    @Query("from Region where (?1 = '' or locate(?1, parentIds) > 0) and locate(name, ?2) = 1")
    Optional<Region> findOneByParentIdsAndName(String parentIds, String name);

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    List<Region> findAllByIndexIsNotNullOrderByIndexAscCodeAsc();

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    Optional<Region> findOneByCode(@NotBlank String code);

}
