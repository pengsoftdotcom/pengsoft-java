package com.pengsoft.basedata.api;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonDetailsService;
import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.Constant;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Authorized
@RestController
@RequestMapping(Constant.API_PREFIX + "/user-details")
public class PersonDetailsApi {

    @Inject
    private PersonDetailsService service;

    @AuthorityChanged
    @PostMapping("save-person")
    public UserDetails savePerson(@RequestBody Person person) {
        return service.savePerson(person);
    }

}
