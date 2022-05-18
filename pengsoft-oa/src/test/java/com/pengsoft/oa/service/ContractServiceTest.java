package com.pengsoft.oa.service;

import java.util.stream.StreamSupport;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.QJobRole;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.system.service.DictionaryItemService;
import com.querydsl.jpa.JPAExpressions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
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

    @Inject
    PersonRepository personRepository;

    @Test
    @WithUserDetails("13368064910")
    void create() {
        final var cashier = staffRepository
                .findOneByPersonIdAndPrimaryTrue(
                        personRepository.findOneByUserId(SecurityUtils.getUserId()).orElseThrow().getId())
                .orElseThrow();
        final var partyA = cashier.getJob().getDepartment().getOrganization();
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

        StreamSupport.stream(staffRepository
                .findAll(root.job.department.eq(cashier.getDepartment())
                        .and(JPAExpressions.selectOne().from(jobRoles, jobRole).where(jobRole.role.code.eq("worker"))
                                .exists()))
                .spliterator(), false).forEach(worker -> {
                    final var contract = new Contract();
                    contract.setStatus(status);
                    contract.setPartyAId(partyA.getId());
                    contract.setPartyAType(partyAType);
                    contract.setPartyBId(worker.getPerson().getId());
                    contract.setPartyBType(partyBType);
                    contract.setBelongsTo(worker.getJob().getDepartment().getOrganization().getId());
                    contract.setControlledBy(worker.getJob().getDepartment().getId());
                    contractService.save(contract);
                });

    }

}
