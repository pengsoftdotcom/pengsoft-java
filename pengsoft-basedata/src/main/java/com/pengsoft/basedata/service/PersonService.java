package com.pengsoft.basedata.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

/**
 * The service interface of {@link Person}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonService extends EntityService<Person, String> {

    /**
     * Delete the {@link Person}'s avatar.
     * 
     * @param person {@link Person}
     * @param asset  {@link Asset}
     * @return {@link Person}'s version
     */
    long deleteAvatarByAsset(Person person, @NotNull Asset asset);

    /**
     * Returns an {@link Optional} of a {@link Person} with the given mobile.
     *
     * @param mobile {@link Person}'s mobile
     */
    Optional<Person> findOneByMobile(@NotBlank String mobile);

    /**
     * Returns an {@link Optional} of a {@link Person} with the given user.
     *
     * @param userId the id of {@link User}.
     */
    Optional<Person> findOneByUserId(@NotNull String userId);

    /**
     * Returns an {@link Optional} of a {@link Person} with the given identity card
     * number.
     *
     * @param identityCardNumber {@link Person}'s identity card number
     */
    Optional<Person> findOneByIdentityCardNumber(@NotBlank String identityCardNumber);

}
