package com.pengsoft.basedata.facade;

import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.IdentityCardService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.system.domain.Asset;

import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PersonFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class PersonFacadeImpl extends EntityFacadeImpl<PersonService, Person, String> implements PersonFacade {

    @Inject
    private IdentityCardService identityCardService;

    @Override
    public Person save(Person person) {
        final var identityCard = person.getIdentityCard();
        if (identityCard != null) {
            identityCardService.save(identityCard);
        }
        super.save(person);
        return person;
    }

    @Override
    public long deleteAvatarByAsset(Person person, Asset asset) {
        return getService().deleteAvatarByAsset(person, asset);
    }

    @Override
    public Optional<Person> findOneByMobile(String mobile) {
        return getService().findOneByMobile(mobile);
    }

    @Override
    public Optional<Person> findOneByUserId(String userId) {
        return getService().findOneByUserId(userId);
    }

    @Override
    public Optional<Person> findOneByIdentityCardNumber(String identityCardNumber) {
        return getService().findOneByIdentityCardNumber(identityCardNumber);
    }

}
