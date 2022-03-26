package com.pengsoft.system.api;

import java.util.List;

import com.pengsoft.support.api.TreeEntityApi;
import com.pengsoft.system.domain.Region;
import com.pengsoft.system.json.RegionWrapper;
import com.pengsoft.system.service.RegionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/region")
public class RegionApi extends TreeEntityApi<RegionService, Region, String> {

    @GetMapping({ "find-all-indexed-cities" })
    public List<RegionWrapper> findAllIndexedCities() {
        return ((RegionService) getService()).findAllIndexedCities().stream().map(RegionWrapper::new).toList();
    }

}
