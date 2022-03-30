package com.pengsoft.oa.messaging;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.facade.PersonFacade;
import com.pengsoft.basedata.facade.StaffFacade;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.messaging.AbstractMessageBuilder;
import com.pengsoft.system.messaging.MessageBuilder;

/**
 * {@link MessageBuilder} for contract confirmation.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class ContractConfirmMessageBuilder extends AbstractMessageBuilder {

    @Inject
    private StaffFacade staffFacade;

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Inject
    private PersonFacade personFacade;

    @Override
    protected String getTemplateCode() {
        return "contract-confirm";
    }

    @Override
    protected List<User> getReceivers(Object[] args, Object result) {
        final var contract = (Contract) args[0];
        final var partyBId = contract.getPartyBId();
        String partyBTypeCode = contract.getPartyBType().getCode();
        if (partyBTypeCode.equals("personal")) {
            return List.of(personFacade.findOne(partyBId)
                    .orElseThrow(() -> getExceptions().entityNotExists(Contract.class, partyBId)).getUser());
        }
        if (partyBTypeCode.equals("organization")) {
            final var jobs = jobRoleRepository.findAllByJobDepartmentOrganizationIdAndRoleCode(partyBId, Role.ORG_ADMIN)
                    .stream().map(JobRole::getJob).toList();
            return staffFacade.findAllByJobIn(jobs).stream().map(Staff::getPerson).map(Person::getUser).toList();
        }
        return List.of();
    }

    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        final var messages = super.build(args, result, types);
        messages.entrySet().stream().flatMap(entry -> entry.getValue().stream())
                .forEach(message -> message.setParams(Map.of("thing1", "test", "phrase2", "待确认")));
        return messages;
    }

}
