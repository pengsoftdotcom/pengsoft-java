package com.pengsoft.system.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.TreeEntityRepository;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.domain.QDictionaryItem;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link DictionaryItem} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface DictionaryItemRepository extends TreeEntityRepository<QDictionaryItem, DictionaryItem, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QDictionaryItem root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns a collection of a {@link DictionaryItem} with the given code.
     *
     * @param code {@link DictionaryType}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<DictionaryItem> findAllByTypeCodeOrderByCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link DictionaryItem} with the given
     * {@linkplain DictionaryType type}, {@linkplain DictionaryItem parent} and
     * code.
     *
     * @param typeId   The id of {@link DictionaryType}.
     * @param parentId The id of {@link DictionaryItem}'s parent.
     * @param code     {@link DictionaryItem}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<DictionaryItem> findOneByTypeIdAndParentIdAndCode(@NotBlank String typeId, String parentId,
            @NotBlank String code);

}
