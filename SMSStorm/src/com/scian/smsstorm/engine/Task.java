package com.scian.smsstorm.engine;

import java.util.UUID;

import com.scian.smsstorm.data.DataCache;
import com.scian.smsstorm.data.FileManager;
import com.scian.smsstorm.util.FileUtil;

public abstract class Task implements Runnable {
    private String taskId;

    public Task() {
        UUID uuid = UUID.randomUUID();
        taskId = uuid.toString();
    }

    public String getTaskId() {
        return taskId;
    }
}
