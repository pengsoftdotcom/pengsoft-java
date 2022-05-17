package com.pengsoft.oa.task;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.service.ContractService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.aspect.TaskExecutor;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;

@Named
public class ContractConfirmTaskHandler implements TaskExecutor {

    @Inject
    private TaskService taskService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private ContractService contractService;

    @Inject
    private PersonService personService;

    @Inject
    private Exceptions exceptions;

    @Override
    public void create(Object[] args, Object result) {
        final var contract = (Contract) args[0];
        final var task = taskService.findOneByTargetId(contract.getId()).orElse(new Task());
        if (contract.getStatus().getCode().equals("confirmed")) {
            task.setPercent(100);
            dictionaryItemService
                    .findOneByTypeCodeAndParentAndCode(TASK_STATUS, null, TASK_STATUS_FINISHED)
                    .ifPresent(task::setStatus);
        } else {
            task.setPercent(0);
            dictionaryItemService.findOneByTypeCodeAndParentAndCode(TASK_STATUS, null, TASK_STATUS_CREATED)
                    .ifPresent(task::setStatus);
        }
        if (StringUtils.isBlank(task.getId())) {
            task.setName("合同确认");
            task.setContent("合同已上传，请前往确认!");
            task.setTargetPath("/oa/contract");
            task.setTargetId(contract.getId());
            final var priority = dictionaryItemService
                    .findOneByTypeCodeAndParentAndCode("task_priority", null, "medium")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
            task.setPriority(priority);
            final var partyB = personService.findOne(contract.getPartyBId())
                    .orElseThrow(() -> exceptions.entityNotExists(Person.class, contract.getPartyBId()));
            task.setCreatedBy(partyB.getUser().getId());
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
    }

    @Override
    public void finish(Object[] args, Object result) {
        Contract contract = null;
        if (args[0] instanceof String id) {
            contract = contractService.findOne(id).orElseThrow(() -> exceptions.entityNotExists(Contract.class, id));
        } else {
            contract = (Contract) args[0];
        }
        final var id = contract.getId();
        final var task = taskService.findOneByTargetId(contract.getId())
                .orElseThrow(() -> exceptions.entityNotExists(Task.class, id));
        taskService.finish(task);
    }

    @Override
    public void delete(Object[] args, Object result) {
        final var predicate = (Predicate) args[0];
        contractService.findAll(predicate, Sort.unsorted())
                .forEach(contract -> taskService.findOneByTargetId(contract.getId()).ifPresent(taskService::delete));
    }

}
