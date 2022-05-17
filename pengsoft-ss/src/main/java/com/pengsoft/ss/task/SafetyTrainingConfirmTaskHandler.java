package com.pengsoft.ss.task;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.facade.SafetyTrainingFacade;
import com.pengsoft.ss.repository.SafetyTrainingParticipantRepository;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.aspect.TaskExecutor;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;
import com.querydsl.core.types.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.data.domain.Sort;

@Named
public class SafetyTrainingConfirmTaskHandler implements TaskExecutor {

    @Inject
    private TaskService taskService;

    @Inject
    private SafetyTrainingFacade safetyTrainingFacade;

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
            final var task = taskService.findOneByTargetId(participant.getId()).orElse(new Task());
            if (participant.getStatus() == null) {
                task.setPercent(0);
                dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode(TASK_STATUS, null, TASK_STATUS_CREATED)
                        .ifPresent(task::setStatus);
            } else {
                task.setPercent(100);
                dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode(TASK_STATUS, null, TASK_STATUS_FINISHED)
                        .ifPresent(task::setStatus);
            }
            if (StringUtils.isBlank(task.getId())) {
                task.setName("安全教育培训确认");
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
                final var priority = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode("task_priority", null, "medium")
                        .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
                task.setPriority(priority);
                if (task.getStatus().getCode().equals(TASK_STATUS_FINISHED)) {
                    task.setFinishedAt(DateUtils.currentDateTime());
                    task.setFinishedBy(SecurityUtils.getUserId());
                }
            } else {
                if (task.getStatus().getCode().equals(TASK_STATUS_CREATED)) {
                    task.setFinishedAt(null);
                    task.setFinishedBy(null);
                }
                if (task.getStatus().getCode().equals(TASK_STATUS_FINISHED) && task.getFinishedAt() == null) {
                    task.setFinishedAt(DateUtils.currentDateTime());
                    task.setFinishedBy(SecurityUtils.getUserId());
                }
            }
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

    @Override
    public void delete(Object[] args, Object result) {
        final var predicate = (Predicate) args[0];
        safetyTrainingFacade.findAll(predicate, Sort.unsorted())
                .forEach(training -> training.getParticipants()
                        .forEach(participant -> taskService.findOneByTargetId(participant.getId())
                                .ifPresent(taskService::delete)));

    }

}
