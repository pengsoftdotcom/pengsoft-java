package com.pengsoft.ss.messaging;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.security.domain.User;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.messaging.AbstractMessageBuilder;
import com.pengsoft.system.messaging.MessageBuilder;

/**
 * {@link MessageBuilder} for safety training confirmation.
 * 
 */
@Named
public class SafetyTrainingConfirmMessageBuilder extends AbstractMessageBuilder {

    @Override
    protected String getTemplateCode() {
        return "safety-training-confirm";
    }

    @Override
    protected List<User> getReceivers(Object[] args, Object result) {
        final var training = (SafetyTraining) args[0];
        return training.getParticipants().stream().map(SafetyTrainingParticipant::getStaff).map(Staff::getPerson)
                .map(Person::getUser).toList();
    }

    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        final var messages = super.build(args, result, types);
        final var training = (SafetyTraining) args[0];
        messages.entrySet().stream().flatMap(entry -> entry.getValue().stream()).forEach(message -> {
            message.setParams(Map.of(
                    "subject", training.getSubject(),
                    "estimatedStartTime", DateUtils.formatDateTime(training.getEstimatedStartTime()),
                    "address", training.getAddress()));
        });
        return messages;
    }

}
