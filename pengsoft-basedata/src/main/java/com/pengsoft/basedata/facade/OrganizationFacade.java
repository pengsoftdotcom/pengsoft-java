package com.pengsoft.basedata.facade;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.support.facade.TreeEntityFacade;

/**
 * The facade interface of {@link Organization}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OrganizationFacade
        extends TreeEntityFacade<OrganizationService, Organization, String>, OrganizationService {

    /**
     * Save the organization entity and the associated entities, and submit the real
     * name authentication.
     *
     * @param organization {@link Organization}
     */
    void submit(@Valid @NotNull Organization organization);

    /**
     * Authenticate the organization.
     *
     * @param organization {@link Organization}
     */
    void authenticate(@NotNull Organization organization, boolean authenticated);

}
