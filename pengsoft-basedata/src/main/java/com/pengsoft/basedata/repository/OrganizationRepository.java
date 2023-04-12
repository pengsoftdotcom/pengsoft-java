package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.QOrganization;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

/**
 * The repository interface of {@link Organization} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OrganizationRepository
        extends TreeEntityRepository<QOrganization, Organization, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QOrganization root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * set the legalRepresentative of the organization.
     * 
     * @param organization
     * @param legalRepresentative
     * @param updatedBy
     */
    @Modifying
    @Query("update Organization o set o.legalRepresentative = ?2, o.updatedBy = ?3, o.updatedAt = now()  where o = ?1")
    void setLegalRepresentative(@NotNull Organization organization, Person legalRepresentative, String updatedBy);

    /**
     * set the legalRepresentative of the organization.
     * 
     * @param organization
     * @param businessLicense
     * @param updatedBy
     */
    @Modifying
    @Query("update Organization o set o.businessLicense = ?2, o.updatedBy = ?3, o.updatedAt = now()  where o = ?1")
    void setBusinessLicense(@NotNull Organization organization, BusinessLicense businessLicense, String updatedBy);

    /**
     * Returns an {@link Optional} of a {@link Organization} with the given name.
     *
     * @param name {@link Organization}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Organization> findOneByName(@NotBlank String name);

    /**
     * 返回可选的客户分页数据
     * 
     * @param supplier 供应商
     * @param pageable 分页参数
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.consumer.id from SupplierConsumer sc where sc.supplier = ?1) and o.id != ?1")
    Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable);

    /**
     * 返回所有可选的客户
     * 
     * @param supplier 供应商
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.consumer.id from SupplierConsumer sc where sc.supplier = ?1) and o.id != ?1")
    List<Organization> findAllAvailableConsumers(Organization supplier);

    /**
     * 返回可选的供应商分页数据
     * 
     * @param consumer 客户
     * @param pageable 分页参数
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.supplier.id from SupplierConsumer sc where sc.consumer = ?1) and o.id != ?1")
    Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable);

    /**
     * 返回所有可选的供应商
     * 
     * @param consumer 客户
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.supplier.id from SupplierConsumer sc where sc.consumer = ?1) and o.id != ?1")
    List<Organization> findAllAvailableSuppliers(Organization consumer);

}
