package com.pengsoft.basedata.service;

import java.util.Optional;

import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link IdentityCard}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface IdentityCardService extends EntityService<IdentityCard, String> {

    /**
     * Returns an {@link Optional} of a {@link IdentityCard} with the given number.
     *
     * @param number {@link IdentityCard}'s number
     */
    Optional<IdentityCard> findOneByNumber(String number);

}
