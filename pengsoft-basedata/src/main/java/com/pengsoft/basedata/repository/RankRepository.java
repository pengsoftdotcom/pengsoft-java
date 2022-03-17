package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.QRank;
import com.pengsoft.basedata.domain.Rank;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Rank} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface RankRepository extends EntityRepository<QRank, Rank, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QRank root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Rank} with the given organization id
     * and
     * name.
     *
     * @param organizationId The id of {@link Rank}'s organization
     * @param name           The {@link Rank}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Rank> findOneByOrganizationIdAndName(@NotBlank String organizationId, String name);

}
