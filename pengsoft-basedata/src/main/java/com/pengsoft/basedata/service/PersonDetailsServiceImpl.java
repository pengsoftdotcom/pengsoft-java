package com.pengsoft.basedata.service;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.PersonDetails;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.service.DefaultUserDetailsServiceImpl;
import com.pengsoft.security.util.SecurityUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PersonDetailsService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class PersonDetailsServiceImpl extends DefaultUserDetailsServiceImpl implements PersonDetailsService {

    @Inject
    private PersonService personService;

    @Override
    public PersonDetails savePerson(@NotNull Person person) {
        final var userDetails = (PersonDetails) SecurityUtils.getUserDetails();
        userDetails.setPerson(personService.save(person));
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final var userDetails = (DefaultUserDetails) super.loadUserByUsername(username);
        return personService.findOneByUserId(userDetails.getUser().getId()).map(person -> {
            final var roles = userDetails.getRoles();
            final var authorities = userDetails.getAuthorities();
            final var primaryRole = userDetails.getPrimaryRole();
            return new PersonDetails(person, roles, primaryRole, authorities);
        }).map(DefaultUserDetails.class::cast).orElse(userDetails);
    }

}
