package com.pengsoft.security.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.validation.Password;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link User}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface UserService extends EntityService<User, String> {

    /**
     * Create user from weixin.
     * 
     * @param weixinMpOpenId The weixin mp open id.
     * @return the created user
     */
    User createFromWeixin(String weixinMpOpenId);

    /**
     * Save one user without validation.
     *
     * @param user {@link User}
     * @return the saved user
     */
    User saveWithoutValidation(User user);

    /**
     * Change password.
     *
     * @param id          The user id.
     * @param oldPassword The old password.
     * @param newPassword The new password.
     */
    void changePassword(@NotBlank String id, @NotBlank String oldPassword, @Password String newPassword);

    /**
     * Reset password.
     *
     * @param id       The user id.
     * @param password The password to be reset.
     */
    void resetPassword(@NotBlank String id, @NotBlank String password);

    /**
     * Grant roles.
     *
     * @param user  The {@link User}.
     * @param roles The {@link Role}s to be granted.
     */
    void grantRoles(@NotNull User user, List<Role> roles);

    /**
     * Set the primary role.
     *
     * @param user The {@link User}.
     * @param role The primary {@link Role}.
     */
    void setPrimaryRole(@NotNull User user, @NotNull Role role);

    /**
     * Save the sign in date and clear the sign in failure count.
     *
     * @param username The username.
     */
    void signInSuccess(String username);

    /**
     * Save the sign in failure count.
     *
     * @param username           The username.
     * @param allowSignInFailure The maximum count of sign in failure.
     */
    void signInFailure(String username, int allowSignInFailure);

    /**
     * Returns an {@link Optional} of a {@link User} with the given username.
     *
     * @param username {@link User}' username.
     */
    Optional<User> findOneByUsername(@NotBlank String username);

    /**
     * Returns an {@link Optional} of a {@link User} with the given mobile.
     *
     * @param mobile {@link User}' mobile.
     */
    Optional<User> findOneByMobile(@NotBlank String mobile);

    /**
     * Returns an {@link Optional} of a {@link User} with the given email.
     *
     * @param email {@link User}' email.
     */
    Optional<User> findOneByEmail(@NotBlank String email);

    /**
     * Returns an {@link Optional} of a {@link User} with the given weixin mp open
     * id.
     *
     * @param weixinMpOpenId {@link User}' weixinMpOpenId.
     */
    Optional<User> findOneByWeixinMpOpenId(@NotBlank String weixinMpOpenId);

}
