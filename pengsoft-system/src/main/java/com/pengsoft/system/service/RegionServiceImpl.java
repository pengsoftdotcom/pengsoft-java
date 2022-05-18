package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Address;
import com.pengsoft.system.domain.Region;
import com.pengsoft.system.repository.RegionRepository;

import org.apache.commons.lang3.RegExUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RegionServiceImpl extends TreeEntityServiceImpl<RegionRepository, Region, String>
        implements RegionService {

    @Override
    public Region save(Region region) {
        findOneByCode(region.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, region)) {
                throw getExceptions().constraintViolated("code", "exists", region.getCode());
            }
        });
        return super.save(region);
    }

    @Override
    public Optional<Region> findOneByCode(String code) {
        return getRepository().findOneByCode(code);
    }

    @Override
    public List<Region> findAllIndexedCities() {
        return getRepository().findAllByIndexIsNotNullOrderByIndexAscCodeAsc();
    }

    @Override
    public Address getAddress(String detail) {
        Address address = new Address();
        while (StringUtils.notEquals(address.getDetail(), detail)) {
            address.setDetail(detail);
            String regionId = (address.getRegion() == null) ? "" : address.getRegion().getId();
            Optional<Region> optional = getRepository().findOneByParentIdsAndName(regionId,
                    detail);
            if (optional.isPresent()) {
                Region region = optional.get();
                address.setRegion(region);
                detail = RegExUtils.replaceFirst(detail, region.getName(), "");
            }
        }
        return address;
    }
}
