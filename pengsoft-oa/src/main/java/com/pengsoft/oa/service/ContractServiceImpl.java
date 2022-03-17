package com.pengsoft.oa.service;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.repository.ContractRepository;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link Contract} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class ContractServiceImpl extends EntityServiceImpl<ContractRepository, Contract, String>
        implements ContractService {

    @Inject
    private StaffRepository staffRepository;

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Override
    public void confirm(Contract contract) {
        if (!SecurityUtils.hasAnyRole(Role.ADMIN, Role.ORG_ADMIN)) {
            final var partyBTypeCode = contract.getPartyBType().getCode();
            final var partyBId = contract.getPartyBId();
            if (partyBTypeCode.equals("person") && StringUtils.notEquals(partyBId, SecurityUtilsExt.getPersonId())) {
                throw new AccessDeniedException("Not authorized");
            }

            if (partyBTypeCode.equals("organization")) {
                final var jobIds = jobRoleRepository
                        .findAllByJobDepartmentOrganizationIdAndRoleCode(partyBId, Role.ORG_ADMIN)
                        .stream().map(JobRole::getJob).map(Job::getId).toList();
                final var staffs = staffRepository.findAllByJobIdIn(jobIds);
                if (staffs.stream().map(Staff::getPerson).map(Person::getUser).map(User::getId)
                        .noneMatch(SecurityUtils.getUserId()::equals)) {
                    throw new AccessDeniedException("Not authorized");
                }
            }
        }

        contract.setConfirmedAt(DateUtils.currentDateTime());
        contract.setConfirmedBy(SecurityUtils.getUserId());
        save(contract);
    }

}
