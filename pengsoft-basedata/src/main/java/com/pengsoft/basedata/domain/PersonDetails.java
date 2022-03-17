package com.pengsoft.basedata.domain;

import java.util.List;

import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.domain.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * The implementer of {@link UserDetails} for {@link Person}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class PersonDetails extends DefaultUserDetails {

    private static final long serialVersionUID = 5556996867443270220L;

    private Person person;

    public PersonDetails(final Person person, final List<Role> roles,
            final Role primaryRole, final List<GrantedAuthority> authorities) {
        super(person.getUser(), roles, primaryRole, authorities);
        this.person = person;
    }

}
