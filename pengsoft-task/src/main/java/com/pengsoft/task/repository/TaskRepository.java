package com.pengsoft.task.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.task.domain.QTask;
import com.pengsoft.task.domain.Task;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Task}
 * based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface TaskRepository extends EntityRepository<QTask, Task, String> {

    /**
     * Returns the task with the given target id.
     * 
     * @param targetId The taget id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Task> findOneByTargetId(@NotBlank String targetId);

}