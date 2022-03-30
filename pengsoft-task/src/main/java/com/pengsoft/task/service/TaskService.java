package com.pengsoft.task.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.task.domain.Task;

/**
 * The service interface of {@link Task}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface TaskService extends EntityService<Task, String> {

    /**
     * Finish the task.
     * 
     * @param task {@link Task}
     */
    void finish(@NotNull Task task);

    /**
     * Returns the task with the given target id.
     * 
     * @param targetId The taget id.
     */
    Optional<Task> findOneByTargetId(@NotBlank String targetId);

}
