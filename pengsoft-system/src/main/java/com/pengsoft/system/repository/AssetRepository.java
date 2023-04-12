package com.pengsoft.system.repository;

import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.QAsset;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;

public interface AssetRepository extends EntityRepository<QAsset, Asset, String>, OwnedRepository {

    @Override
    default void customize(QuerydslBindings bindings, QAsset root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.originalName).first(StringExpression::contains);
        bindings.bind(root.contentLength).first(NumberExpression::loe);
    }

}
