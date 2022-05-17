package com.pengsoft.ss.task;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.ss.domain.QualityCheck;
import com.pengsoft.ss.facade.QualityCheckFacade;
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
public class QualityCheckRiskTaskHandler implements TaskExecutor {

    @Inject
    private TaskService taskService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private Exceptions exceptions;

    @Inject
    private QualityCheckFacade qualityCheckFacade;

    @Override
    public void create(Object[] args, Object result) {
        final var check = (QualityCheck) args[0];
        if (StringUtils.equals(check.getStatus().getCode(), "risk")) {
            final var task = new Task();
            task.setName("质量隐患整改");
            task.setContent(check.getProject().getName() + "发现质量隐患，请前往整改!");
            task.setTargetPath("/ss/quality-check");
            task.setTargetId(check.getId());
            final var status = dictionaryItemService.findOneByTypeCodeAndParentAndCode("task_status", null, "created")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_status::created"));
            task.setStatus(status);
            final var priority = dictionaryItemService.findOneByTypeCodeAndParentAndCode("task_priority", null, "high")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
            task.setPriority(priority);
            task.setCreatedBy(check.getProject().getBuManager().getPerson().getUser().getId());
            taskService.save(task);
        } else {
            log.info("task not created cause the quality check status is safe");
        }

    }

    @Override
    public void finish(Object[] args, Object result) {
        final var check = (QualityCheck) args[0];
        final var task = taskService.findOneByTargetId(check.getId())
                .orElseThrow(() -> exceptions.entityNotExists(Task.class, check.getId()));
        taskService.finish(task);
    }

    @Override
    public void delete(Object[] args, Object result) {
        final var predicate = (Predicate) args[0];
        qualityCheckFacade.findAll(predicate, Sort.unsorted())
                .forEach(check -> taskService.findOneByTargetId(check.getId()).ifPresent(taskService::delete));
    }

}
