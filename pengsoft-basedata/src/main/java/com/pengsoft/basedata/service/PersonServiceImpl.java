package com.pengsoft.basedata.service;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.repository.UserRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PersonService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PersonServiceImpl extends EntityServiceImpl<PersonRepository, Person, String> implements PersonService {

    @Inject
    private AssetService assetService;

    @Inject
    private UserRepository userRepository;

    @Lazy
    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public Person save(final Person person) {
        final var mobile = person.getMobile();
        if (StringUtils.isNotBlank(mobile)) {
            getRepository().findOneByMobile(mobile).ifPresent(source -> {
                if (EntityUtils.notEquals(source, person)) {
                    fieldValueExists("mobile", mobile);
                }
            });
            if (StringUtils.isBlank(person.getId())) {
                userRepository.findOneByMobile(mobile)
                        .ifPresent(user -> fieldValueExists("user.mobile", user.getMobile()));
                userRepository.findOneByUsername(mobile)
                        .ifPresent(user -> fieldValueExists("user.username", user.getUsername()));
                final var user = new User(mobile, passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setMobile(mobile);
                person.setUser(userRepository.save(user));
            }
        }

        if (StringUtils.isBlank(person.getNickname())) {
            person.setNickname("*" + person.getName().substring(1));
        }

        getRepository().save(person);
        if (person.getIdentityCard() != null) {
            getRepository().updateIdentityCardNumber(person.getId(), person.getIdentityCard().getNumber());
        }
        return person;
    }

    private void fieldValueExists(String name, String value) {
        throw getExceptions().constraintViolated(name, "exists", value);
    }

    @Override
    public void delete(Person person) {
        super.delete(person);
        if (person.getAvatar() != null) {
            assetService.delete(person.getAvatar());
        }
    }

    @Override
    public long deleteAvatarByAsset(Person person, Asset asset) {
        if (person != null) {
            person.setAvatar(null);
            super.save(person);
        }
        assetService.delete(asset);
        return person == null ? -1 : person.getVersion() + 1;
    }

    @Override
    public Optional<Person> findOneByMobile(final String mobile) {
        return getRepository().findOneByMobile(mobile);
    }

    @Override
    public Optional<Person> findOneByUserId(final String userId) {
        return getRepository().findOneByUserId(userId);
    }

    @Override
    public Optional<Person> findOneByIdentityCardNumber(String identityCardNumber) {
        return getRepository().findOneByIdentityCardNumber(identityCardNumber);
    }

}
