package com.pengsoft.basedata.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.domain.StaffDetails;
import com.pengsoft.security.domain.DefaultUserDetails;

/**
 * User details service for {@link Staff}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StaffDetailsService extends PersonDetailsService {

    /**
     * Set the primary job.
     *
     * @param job The primary job.
     * @return {@link StaffDetails}
     */
    DefaultUserDetails setPrimaryJob(@NotNull Job job);

}
