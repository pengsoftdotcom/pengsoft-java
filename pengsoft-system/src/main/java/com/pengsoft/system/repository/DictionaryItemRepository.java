package com.pengsoft.system.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.TreeEntityRepository;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.QDictionaryItem;
import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryItemRepository
        extends TreeEntityRepository<QDictionaryItem, DictionaryItem, String> {

    @Override
    default void customize(QuerydslBindings bindings, QDictionaryItem root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringExpression::contains);
        bindings.bind(root.name).first(StringExpression::contains);
    }

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    Optional<DictionaryItem> findOneByTypeCodeAndParentIdAndCode(@NotBlank String typeCode, String parentId,
            @NotBlank String code);

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    List<DictionaryItem> findAllByTypeCodeOrderByCode(@NotBlank String typeCode);

}
