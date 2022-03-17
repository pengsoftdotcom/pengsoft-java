package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.support.service.TreeEntityService;
import com.pengsoft.system.domain.Asset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The service interface of {@link Organization}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OrganizationService extends TreeEntityService<Organization, String> {

    /**
     * set the legalRepresentative of the organization.
     * 
     * @param organization
     * @param legalRepresentative
     */
    void setLegalRepresentative(@NotNull Organization organization, Person legalRepresentative);

    /**
     * set the businessLicense of the organization.
     * 
     * @param organization
     * @param businessLicense
     */
    void setBusinessLicense(@NotNull Organization organization, BusinessLicense businessLicense);

    /**
     * Delete the {@link Organization}'s logo.
     * 
     * @param organization {@link Organization}
     * @param asset        {@link Asset}
     * @return {@link Organization}'s version
     */
    long deleteLogoByAsset(Organization organization, @NotNull Asset asset);

    /**
     * Returns an {@link Optional} of a {@link Organization} with the given name.
     *
     * @param name {@link Organization}'s name
     */
    Optional<Organization> findOneByName(String name);

    /**
     * 返回可选的客户分页数据
     * 
     * @param supplier 供应商
     * @param pageable 分页参数
     */
    Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable);

    /**
     * 返回所有可选的客户
     * 
     * @param supplier 供应商
     */
    List<Organization> findAllAvailableConsumers(Organization supplier);

    /**
     * 返回可选的供应商分页数据
     * 
     * @param consumer 客户
     * @param pageable 分页参数
     */
    Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable);

    /**
     * 返回所有可选的供应商
     * 
     * @param consumer 客户
     */
    List<Organization> findAllAvailableSuppliers(Organization consumer);

}
