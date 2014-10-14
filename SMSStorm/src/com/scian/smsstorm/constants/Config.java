package com.scian.smsstorm.constants;

public class Config {
    public static final int DATA_VERSION = 1;

    public static final String TAG = "scian";
    public static final String SENT = "SMS_SENT";
    public static final String DELIVERED = "SMS_DELIVERED";

    public static final String NUMBER = "SMS_NUMBER";
    public static final String KEY = "tag";
    public static final String TYPE = "sent";
    public static final String FILE_DELIVERED = "delivered";
    public static final String FILE_FAILED = "failed";
    public static final String FILE_RAW = "raw";
    public static final String FILE__TEST = "test";
    public static final String FILE_SENDING = "sending";

    public static final String BUNDLE_KEY_TAG = "BUNDLE_KEY_TAG";

    public static final int LIST_TYPE_ALL = 0;
    public static final int LIST_TYPE_SENT = 1;
    public static final int LIST_TYPE_SENDING = 2;
    public static final int LIST_TYPE_SENT_FAILED = 3;
    public static final int LIST_TYPE_DELIVERED_FAILED = 4;
    public static final int LIST_TYPE_DELIVERED = 5;

    public static final int TOAST_DURATION = 2000;

    public static final int SMS_SEND_SLEEP_TIME = 50;

    public static final int SERVICE_STATE_RUNNING = 1;
    public static final int SERVICE_STATE_IDLE = 0;
    public static final int SERVICE_STATE_PAUSE = 2;

}
