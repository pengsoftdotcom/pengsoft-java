package com.pengsoft.task.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.task.domain.Task;
import com.pengsoft.task.service.TaskService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Task}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/task/task")
public class TaskApi extends EntityApi<TaskService, Task, String> {

    @PutMapping("finish")
    public void finish(@RequestParam("id") Task task) {
        getService().finish(task);
    }

}
