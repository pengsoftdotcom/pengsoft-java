package com.pengsoft.oa.messaging;

import java.util.HashMap;
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
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.domain.MessageTemplate;
import com.pengsoft.system.domain.SmsMessage;
import com.pengsoft.system.domain.SmsMessageTemplate;
import com.pengsoft.system.messaging.MessageBuilder;
import com.pengsoft.system.service.CompositeMessageTemplateService;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

/**
 * {@link MessageBuilder} for contract confirmation.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class ContractConfirmMessageBuilder implements MessageBuilder {

    private static final String TEMPLATE_CODE = "contract-confirm";

    @Inject
    private CompositeMessageTemplateService compositeMessageTemplateService;

    @Inject
    private StaffFacade staffFacade;

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Inject
    private PersonFacade personFacade;

    @Inject
    private UserService userService;

    @Inject
    private Exceptions exceptions;

    @SneakyThrows
    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        Contract contract = (Contract) args[0];
        final var compositeMessageTemplate = compositeMessageTemplateService.findOneByCode(TEMPLATE_CODE)
                .orElseThrow(() -> exceptions.entityNotExists(TEMPLATE_CODE));
        Map<String, List<Message>> messages = new HashMap<>();
        final var sender = userService.findOneByUsername("admin")
                .orElseThrow(() -> exceptions.entityNotExists("admin"));
        for (String type : types) {
            MessageTemplate messageTemplate = (MessageTemplate) MethodUtils.invokeMethod(compositeMessageTemplate,
                    "get" + StringUtils.capitalize(type));
            if (messageTemplate != null) {
                messages.put(type, getReceivers(contract).stream()
                        .map(receiver -> {
                            final var message = messageTemplate.toMessage(sender, receiver);
                            if (message instanceof SmsMessage smsMessage) {
                                smsMessage.setTemplate((SmsMessageTemplate) messageTemplate);
                            }
                            return message;
                        }).toList());
            }
        }
        return messages;
    }

    private List<User> getReceivers(Contract contract) {
        final var partyBId = contract.getPartyBId();
        String partyBTypeCode = contract.getPartyBType().getCode();
        if (partyBTypeCode.equals("peronal")) {
            return List.of(personFacade.findOne(partyBId)
                    .orElseThrow(() -> exceptions.entityNotExists(partyBId)).getUser());
        }
        if (partyBTypeCode.equals("organization")) {
            final var jobs = jobRoleRepository.findAllByJobDepartmentOrganizationIdAndRoleCode(partyBId, Role.ORG_ADMIN)
                    .stream().map(JobRole::getJob).toList();
            return staffFacade.findAllByJobIn(jobs).stream().map(Staff::getPerson).map(Person::getUser).toList();
        }
        return List.of();
    }

}
