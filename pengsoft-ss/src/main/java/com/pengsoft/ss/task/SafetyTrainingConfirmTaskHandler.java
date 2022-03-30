package com.pengsoft.ss.task;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.repository.SafetyTrainingParticipantRepository;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.aspect.TaskExecutor;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;

import org.apache.commons.text.StringSubstitutor;

@Named
public class SafetyTrainingConfirmTaskHandler implements TaskExecutor {

    @Inject
    private TaskService taskService;

    @Inject
    private SafetyTrainingParticipantRepository safetyTrainingParticipantRepository;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private Exceptions exceptions;

    @Override
    public void create(Object[] args, Object result) {
        final var training = (SafetyTraining) args[0];
        final var participants = safetyTrainingParticipantRepository.findAllByTrainingId(training.getId());
        participants.forEach(participant -> {
            final var task = new Task();
            task.setName("安全培训确认");
            final var params = new HashMap<String, String>();
            params.put("subject", training.getSubject());
            params.put("estimatedStartTime",
                    DateUtils.formatDateTime(training.getEstimatedStartTime()).substring(0, 16));
            params.put("address", training.getAddress());
            final var substitutor = new StringSubstitutor(params);
            task.setContent(substitutor.replace("${subject}将于${estimatedStartTime}在${address}开始，请确认"));
            task.setTargetPath("/ss/safety-training-participant");
            task.setTargetId(participant.getId());
            task.setCreatedBy(participant.getStaff().getPerson().getUser().getId());
            final var status = dictionaryItemService.findOneByTypeCodeAndParentAndCode("task_status", null, "created")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_status::created"));
            task.setStatus(status);
            final var priority = dictionaryItemService
                    .findOneByTypeCodeAndParentAndCode("task_priority", null, "medium")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
            task.setPriority(priority);
            taskService.save(task);
        });

    }

    @Override
    public void finish(Object[] args, Object result) {
        final var participant = (SafetyTrainingParticipant) args[0];
        final var task = taskService.findOneByTargetId(participant.getId())
                .orElseThrow(() -> exceptions.entityNotExists(Task.class, participant.getId()));
        taskService.finish(task);
    }

}
