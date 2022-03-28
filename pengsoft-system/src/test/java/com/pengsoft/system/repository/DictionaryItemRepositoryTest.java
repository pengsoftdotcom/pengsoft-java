package com.pengsoft.system.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system" })
class DictionaryItemRepositoryTest {

    @Inject
    DictionaryItemRepository repository;

    @Test
    void findOneByTypeAndParentAndCode() {
        repository.findOneByTypeCodeAndParentIdAndCode("1", "1", "1");
    }

}
