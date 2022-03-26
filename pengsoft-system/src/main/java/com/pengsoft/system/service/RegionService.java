package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.TreeEntityService;
import com.pengsoft.system.domain.Address;
import com.pengsoft.system.domain.Region;

public interface RegionService extends TreeEntityService<Region, String> {

    Optional<Region> findOneByCode(@NotBlank String code);

    List<Region> findAllIndexedCities();

    Address getAddress(String address);

}
