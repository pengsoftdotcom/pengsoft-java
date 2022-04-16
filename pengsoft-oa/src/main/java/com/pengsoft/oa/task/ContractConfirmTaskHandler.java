package com.pengsoft.oa.task;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.service.ContractService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.aspect.TaskExecutor;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;

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
        final var id = ((Contract) args[0]).getId();
        final var contract = contractService.findOne(id)
                .orElseThrow(() -> exceptions.entityNotExists(Contract.class, id));
        if (contract.getStatus() != null && contract.getStatus().getCode().equals("unconfirmed")) {
            final var task = new Task();
            task.setName("合同确认");
            task.setContent("合同已上传，请前往确认!");
            task.setTargetPath("/oa/contract");
            task.setTargetId(contract.getId());
            final var status = dictionaryItemService.findOneByTypeCodeAndParentAndCode("task_status", null, "created")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_status::created"));
            task.setStatus(status);
            final var priority = dictionaryItemService
                    .findOneByTypeCodeAndParentAndCode("task_priority", null, "medium")
                    .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
            task.setPriority(priority);
            final var partyB = personService.findOne(contract.getPartyBId())
                    .orElseThrow(() -> exceptions.entityNotExists(Person.class, contract.getPartyBId()));
            task.setCreatedBy(partyB.getUser().getId());
            taskService.save(task);
        }
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

}
