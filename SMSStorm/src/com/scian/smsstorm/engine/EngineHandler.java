package com.scian.smsstorm.engine;

public class EngineHandler {

    private static EngineHandler instance;

    private Engine mEngine;

    static {
        instance = new EngineHandler();
    }

    private EngineHandler() {
        mEngine = new Engine();
    }

    public static EngineHandler getaInstance() {
        return instance;
    }

    public void excute(Task task) {
        if (mEngine != null) {
            mEngine.excute(task);
        }
    }
}
