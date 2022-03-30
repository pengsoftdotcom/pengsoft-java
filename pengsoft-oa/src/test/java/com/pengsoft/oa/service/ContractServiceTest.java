package com.pengsoft.oa.service;

import java.util.stream.StreamSupport;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.QJobRole;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.system.service.DictionaryItemService;
import com.querydsl.jpa.JPAExpressions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "iot", "acs", "oa" })
class ContractServiceTest {

    @Inject
    ContractService contractService;

    @Inject
    DictionaryItemService dictionaryItemService;

    @Inject
    OrganizationService organizationService;

    @Inject
    StaffRepository staffRepository;

    @Test
    void create() {
        final var partyA = organizationService.findOne("8eba908e-041c-49ad-b6d2-b83dbe2427d0").orElseThrow();
        final var partyAType = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode("contract_party_type", null, "organization").orElseThrow();
        final var partyBType = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode("contract_party_type", null, "personal").orElseThrow();
        final var status = dictionaryItemService
                .findOneByTypeCodeAndParentAndCode("contract_status", null, "not_uploaded")
                .orElseThrow();
        final var root = QStaff.staff;
        final var jobRoles = root.job.jobRoles;
        final var jobRole = QJobRole.jobRole;
        StreamSupport.stream(staffRepository.findAll(
                JPAExpressions.selectOne().from(jobRoles, jobRole).where(jobRole.role.code.eq("worker")).exists())
                .spliterator(), false).forEach(staff -> {
                    final var contract = new Contract();
                    contract.setStatus(status);
                    contract.setPartyAId(partyA.getId());
                    contract.setPartyAType(partyAType);
                    contract.setPartyBId(staff.getPerson().getId());
                    contract.setPartyBType(partyBType);
                    contract.setBelongsTo(staff.getJob().getDepartment().getOrganization().getId());
                    contract.setControlledBy(staff.getJob().getDepartment().getId());
                    contract.setCreatedBy("7a238c71-3260-4263-8f30-57b7ea68c29e");
                    contract.setUpdatedBy("7a238c71-3260-4263-8f30-57b7ea68c29e");
                    contractService.save(contract);
                });

    }

}
