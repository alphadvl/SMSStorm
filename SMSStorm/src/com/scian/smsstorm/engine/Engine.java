package com.scian.smsstorm.engine;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class Engine {

    ThreadPoolExecutor executor;

    public Engine() {
        executor = new ScheduledThreadPoolExecutor(2);
    }

    public void excute(Task task) {
        if (executor != null) {
            executor.execute(task);
        }
    }
}
