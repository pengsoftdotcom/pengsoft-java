package com.pengsoft.task.service;

import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.repository.DictionaryItemRepository;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.repository.TaskRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of
 * {@link TaskService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class TaskServiceImpl extends EntityServiceImpl<TaskRepository, Task, String> implements TaskService {

    private static final String STATUS_FINISHED = "finished";

    @Inject
    private DictionaryItemRepository dictionaryItemRepository;

    @Override
    public void finish(Task task) {
        if (StringUtils.equals(task.getStatus().getCode(), STATUS_FINISHED)) {
            throw new BusinessException("task.finish.already", task.getName());
        }
        final var status = dictionaryItemRepository
                .findOneByTypeCodeAndParentIdAndCode("task_status", null, STATUS_FINISHED)
                .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, STATUS_FINISHED));
        task.setStatus(status);
        task.setFinishedBy(SecurityUtils.getUserId());
        task.setFinishedAt(DateUtils.currentDateTime());
        save(task);
    }

    @Override
    public Optional<Task> findOneByTargetId(String targetId) {
        return getRepository().findOneByTargetId(targetId);
    }

}
