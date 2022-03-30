package com.pengsoft.task.aspect;

public interface TaskExecutor {

    void create(Object[] args, Object result);

    void finish(Object[] args, Object result);

}
