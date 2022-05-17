package com.pengsoft.oa.task;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.repository.PayrollDetailRepository;
import com.pengsoft.oa.service.PayrollRecordService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;
import com.pengsoft.task.aspect.TaskExecutor;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;

@Named
public class PayrollDetailConfirmTaskHandler implements TaskExecutor {

    @Inject
    private TaskService taskService;

    @Inject
    private DictionaryItemService dictionaryItemService;

    @Inject
    private PayrollRecordService payrollRecordService;

    @Inject
    private PayrollDetailRepository payrollDetailRepository;

    @Inject
    private Exceptions exceptions;

    @Override
    public void create(Object[] args, Object result) {
        final var id = ((PayrollRecord) args[0]).getId();
        payrollDetailRepository.findAllByPayrollId(id).forEach(payrollDetail -> {
            if (payrollDetail.getConfirmedAt() == null) {
                final var task = new Task();
                task.setName("工资确认");
                task.setContent("工资已发放，请前往确认!");
                task.setTargetPath("/oa/payroll-detail");
                task.setTargetId(payrollDetail.getId());
                final var status = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode("task_status", null, "created")
                        .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_status::created"));
                task.setStatus(status);
                final var priority = dictionaryItemService
                        .findOneByTypeCodeAndParentAndCode("task_priority", null, "medium")
                        .orElseThrow(() -> exceptions.entityNotExists(DictionaryItem.class, "task_priority::high"));
                task.setPriority(priority);
                task.setCreatedBy(payrollDetail.getStaff().getPerson().getUser().getId());
                taskService.save(task);
            }
        });
    }

    @Override
    public void finish(Object[] args, Object result) {
        PayrollDetail payrollDetail = null;
        if (args[0] instanceof String id) {
            payrollDetail = payrollDetailRepository.findById(id)
                    .orElseThrow(() -> exceptions.entityNotExists(PayrollDetail.class, id));
        } else {
            payrollDetail = (PayrollDetail) args[0];
        }
        final var id = payrollDetail.getId();
        final var task = taskService.findOneByTargetId(payrollDetail.getId())
                .orElseThrow(() -> exceptions.entityNotExists(Task.class, id));
        taskService.finish(task);
    }

    @Override
    public void delete(Object[] args, Object result) {
        final var predicate = (Predicate) args[0];
        payrollRecordService.findAll(predicate, Sort.unsorted())
                .forEach(payroll -> payroll.getDetails().forEach(
                        detail -> taskService.findOneByTargetId(detail.getId()).ifPresent(taskService::delete)));
    }

}
