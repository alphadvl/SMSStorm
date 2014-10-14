package com.scian.smsstorm.data.bean;

public class BaseItem {

    public static int SMS_STATE_NONE;
    public static int SMS_STATE_SENDING;
    public static int SMS_STATE_SENT;
    public static int SMS_STATE_DELIVERED;

    protected String number;
    protected int state;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
