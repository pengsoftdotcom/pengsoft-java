package com.pengsoft.ss.task;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.facade.SafetyCheckFacade;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.aspect.TaskExecutor;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
public class SafetyCheckRiskTaskHandler implements TaskExecutor {

    @Inject
    private TaskService taskService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private SafetyCheckFacade safetyCheckFacade;

    @Inject
    private Exceptions exceptions;

    @Override
    public void create(Object[] args, Object result) {
        final var check = (SafetyCheck) args[0];
        if (StringUtils.equals(check.getStatus().getCode(), "risk")) {
            final var task = StringUtils.isBlank(check.getId()) ? new Task()
                    : taskService.findOneByTargetId(check.getId()).orElse(new Task());
            dictionaryItemService.findOneByTypeCodeAndParentAndCode("task_status", null, "created")
                    .ifPresent(task::setStatus);
            if (StringUtils.isBlank(task.getId())) {
                task.setName("安全质量隐患整改");
                task.setContent(check.getProject().getShortName() + "发现安全质量隐患，请前往整改!");
                task.setTargetPath("/ss/safety-check");
                task.setTargetId(check.getId());
                final var priority = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode("task_priority", null, "high")
                        .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
                task.setPriority(priority);
                task.setCreatedBy(check.getProject().getBuManager().getPerson().getUser().getId());
            } else {
                task.setPercent(0);
                task.setFinishedAt(null);
                task.setFinishedBy(null);
            }
            taskService.save(task);
        } else {
            log.info("task not created cause the safety check status is safe");
        }

    }

    @Override
    public void finish(Object[] args, Object result) {
        final var check = (SafetyCheck) args[0];
        final var task = taskService.findOneByTargetId(check.getId())
                .orElseThrow(() -> exceptions.entityNotExists(Task.class, check.getId()));
        taskService.finish(task);
    }

    @Override
    public void delete(Object[] args, Object result) {
        final var predicate = (Predicate) args[0];
        safetyCheckFacade.findAll(predicate, Sort.unsorted())
                .forEach(check -> taskService.findOneByTargetId(check.getId()).ifPresent(taskService::delete));
    }

}
