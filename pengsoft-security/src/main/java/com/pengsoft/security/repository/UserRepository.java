package com.pengsoft.security.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.QUser;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link User} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends EntityRepository<QUser, User, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QUser root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.username).first(StringPath::contains);
        bindings.bind(root.expiredAt).first(DateTimePath::before);
    }

    /**
     * Reset password.
     *
     * @param id       The user id
     * @param password The password to be reset.
     */
    @Modifying
    @Query("update User set password = ?2 where id = ?1")
    void resetPassword(@NotBlank final String id, @NotBlank final String password);

    /**
     * Update the user's mobile
     *
     * @param username The user's username
     * @param mobile   The user's mobile
     */
    @Modifying
    @Query("update User set mobile = ?2 where username = ?1")
    void updateMobile(String username, String mobile);

    /**
     * Update the user's mpOpenid
     *
     * @param username The user's username
     * @param mpOpenid The user's mobile
     */
    @Modifying
    @Query("update User set mpOpenid = ?2 where username = ?1")
    void updateMpOpenid(String username, String mpOpenid);

    /**
     * Update the user's email
     *
     * @param username The user's username
     * @param email    The user's email
     */
    @Modifying
    @Query("update User set email = ?2 where username = ?1")
    void updateEmail(String username, String email);

    /**
     * Returns an {@link Optional} of a {@link User} with the given username.
     *
     * @param username {@link User}'s username
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<User> findOneByUsername(@NotBlank String username);

    /**
     * Returns an {@link Optional} of a {@link User} with the given mobile.
     *
     * @param mobile {@link User}'s mobile
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<User> findOneByMobile(@NotBlank String mobile);

    /**
     * Returns an {@link Optional} of a {@link User} with the given email.
     *
     * @param email {@link User}'s email
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<User> findOneByEmail(@NotBlank String email);

    /**
     * Returns an {@link Optional} of a {@link User} with the given weixin mp open
     * id.
     *
     * @param mpOpenid {@link User}'s mpOpenid
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<User> findOneByMpOpenid(@NotBlank String mpOpenid);

}
