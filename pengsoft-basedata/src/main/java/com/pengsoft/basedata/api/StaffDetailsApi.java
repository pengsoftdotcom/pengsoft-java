package com.pengsoft.basedata.api;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.StaffDetails;
import com.pengsoft.basedata.service.StaffDetailsService;
import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.Constant;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link StaffDetails}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Authorized(writable = true)
@RestController
@RequestMapping(Constant.API_PREFIX + "/user-details")
public class StaffDetailsApi {

    @Inject
    private StaffDetailsService service;

    @AuthorityChanged
    @PostMapping("set-primary-job")
    public UserDetails setPrimaryJob(@RequestParam("id") final Job job) {
        return service.setPrimaryJob(job);
    }

}
