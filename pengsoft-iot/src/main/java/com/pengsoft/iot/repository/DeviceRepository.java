package com.pengsoft.iot.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.iot.domain.Device;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.QDevice;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link Device} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface DeviceRepository extends EntityRepository<QDevice, Device, String>, OwnedExtRepository {

    /**
     * Returns an {@link Optional} of a {@link Device} with the given code.
     *
     * @param code {@link Device}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Device> findOneByCode(@NotBlank String code);

    /**
     * Returns the page data with the given group.
     * 
     * @param group    {@link Group}
     * @param name     The name of the device
     * @param pageable {@link Pageable}
     */
    @Query("from Device d where d in (select device from GroupDevice where group = ?1) and d.name like ?2")
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Page<Device> findPageByGroupAndName(@NotNull Group group, String name, Pageable pageable);

}
