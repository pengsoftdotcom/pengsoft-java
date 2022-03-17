package com.pengsoft.basedata.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.PersonDetails;
import com.pengsoft.security.service.DefaultUserDetailsService;

/**
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonDetailsService extends DefaultUserDetailsService {

    /**
     * save person info
     *
     * @param person The person info.
     * @Return {@link PersonDetails}
     */
    PersonDetails savePerson(@NotNull Person person);

}
