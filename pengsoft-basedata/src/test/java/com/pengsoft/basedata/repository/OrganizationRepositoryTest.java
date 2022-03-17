package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class OrganizationRepositoryTest {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    PersonRepository personRepository;

    @Test
    void findPageExcludeConsumerBySupplier() {
        organizationRepository.findOneByName("重庆鹏软科技有限公司")
                .ifPresent(organization -> organizationRepository.findPageOfAvailableConsumers(organization, null));
    }

    @Test
    void findPageExcludeSupplierByConsumer() {
        organizationRepository.findOneByName("重庆鹏软科技有限公司").ifPresent(organization -> organizationRepository
                .findPageOfAvailableSuppliers(organization, PageRequest.of(0, 20)));
    }

}
