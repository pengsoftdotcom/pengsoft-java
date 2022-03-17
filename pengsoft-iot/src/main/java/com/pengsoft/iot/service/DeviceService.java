package com.pengsoft.iot.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.iot.domain.Device;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.security.domain.Authority;
import com.pengsoft.support.service.EntityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The service interface of {@link Device}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface DeviceService extends EntityService<Device, String> {

    /**
     * Save the device and the relation with groups.
     * 
     * @param device {@link Device}
     * @param groups The group ids seperated by commas
     */
    void saveWithGroups(@Valid @NotNull Device device, @NotBlank String groups);

    /**
     * Grant grouops.
     *
     * @param device The {@link Device}
     * @param groups The groups to be granted.
     */
    void grantGroups(@NotNull Device device, List<Group> groups);

    /**
     * Returns an {@link Optional} of a {@link Device} with the given code.
     *
     * @param code {@link Authority}'s code
     */
    Optional<Device> findOneByCode(@NotBlank String code);

    /**
     * Returns the page data by the given group.
     * 
     * @param group    {@link Group}
     * @param name     The name of the device
     * @param pageable {@link Pageable}
     */
    Page<Device> findPageByGroupAndName(@NotNull Group group, String name, Pageable pageable);

}
