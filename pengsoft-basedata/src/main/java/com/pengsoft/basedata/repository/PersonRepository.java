package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.QPerson;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.support.validation.IdentityNumber;
import com.pengsoft.support.validation.Mobile;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Person} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PersonRepository extends EntityRepository<QPerson, Person, String>, OwnedRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QPerson root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
        bindings.bind(root.nickname).first(StringPath::contains);
        bindings.bind(root.mobile).first(StringPath::contains);
        bindings.bind(root.identityCardNumber).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Person} with the given mobile.
     *
     * @param mobile {@link Person}'s mobile
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Person> findOneByMobile(@NotBlank String mobile);

    /**
     * Returns an {@link Optional} of a {@link Person} with the given user id.
     *
     * @param userId The id of {@link User}.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Person> findOneByUserId(@NotBlank String userId);

    /**
     * Returns an {@link Optional} of a {@link Person} with the given identity card
     * number.
     *
     * @param identityCardNumber {@link Person}'s identity card number
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Person> findOneByIdentityCardNumber(@NotBlank String identityCardNumber);

    /**
     * 更新身份证号
     * 
     * @param id                 The person's id.
     * @param identityCardNumber The person's identity card number.
     */
    @Modifying
    @Query("update Person set identityCardNumber = ?2, dateUpdated = now() where id = ?1")
    void updateIdentityCardNumber(@NotBlank String id, @IdentityNumber String identityCardNumber);

    /**
     * 更新手机号码
     * 
     * @param id     The person's id.
     * @param mobile The person's mobile
     */
    @Modifying
    @Query("update Person set mobile = ?2, dateUpdated = now() where id = ?1")
    void updateMobile(@NotBlank String id, @Mobile String mobile);

}
