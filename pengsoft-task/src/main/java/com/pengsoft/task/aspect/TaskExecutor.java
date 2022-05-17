package com.pengsoft.task.aspect;

public interface TaskExecutor {

    public static final String TASK_STATUS = "task_status";

    public static final String TASK_STATUS_CREATED = "created";

    public static final String TASK_STATUS_FINISHED = "finished";

    void create(Object[] args, Object result);

    void finish(Object[] args, Object result);

    void delete(Object[] args, Object result);

}
