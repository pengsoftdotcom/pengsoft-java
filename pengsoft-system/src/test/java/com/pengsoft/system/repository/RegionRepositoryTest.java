package com.pengsoft.system.repository;

import javax.inject.Inject;

import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Address;

import org.apache.commons.lang3.RegExUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system" })
class RegionRepositoryTest {

    @Inject
    RegionRepository repository;

    @Inject
    ObjectMapper objectMapper;

    @Test
    void recognizeAddress() throws Exception {
        var detail = "重庆市沙坪坝区大石村462号2-1";
        var address = new Address();
        while (StringUtils.notEquals(address.getDetail(), detail)) {
            address.setDetail(detail);
            var regionId = address.getRegion() == null ? "" : address.getRegion().getId();
            var optional = repository.findOneByParentIdsAndName(regionId, detail);
            if (optional.isPresent()) {
                var region = optional.get();
                address.setRegion(region);
                detail = RegExUtils.replaceFirst(detail, region.getName(), "");
            }
        }
        System.out.println(objectMapper.writeValueAsString(address));
    }

}
