package com.scian.smsstorm.data.bean;

public class Log {

    private long tag;
    private String content;
    private int total;
    private int sentCount;
    private int failedSentCount;
    private int deliveredCount;
    private int failedDeliveredCount;

    private String totalFile;
    private String sentFile;
    private String failedSentFile;
    private String deliveredFile;
    private String failedDeliveredFile;

    public long getTag() {
        return tag;
    }

    public void setTag(long tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public int getFailedSentCount() {
        return failedSentCount;
    }

    public void setFailedSentCount(int failedSentCount) {
        this.failedSentCount = failedSentCount;
    }

    public int getDeliveredCount() {
        return deliveredCount;
    }

    public void setDeliveredCount(int deliveredCount) {
        this.deliveredCount = deliveredCount;
    }

    public int getFailedDeliveredCount() {
        return failedDeliveredCount;
    }

    public void setFailedDeliveredCount(int failedDeliveredCount) {
        this.failedDeliveredCount = failedDeliveredCount;
    }

    public String getTotalFile() {
        return totalFile;
    }

    public void setTotalFile(String totalFile) {
        this.totalFile = totalFile;
    }

    public String getSentFile() {
        return sentFile;
    }

    public void setSentFile(String sentFile) {
        this.sentFile = sentFile;
    }

    public String getFailedSentFile() {
        return failedSentFile;
    }

    public void setFailedSentFile(String failedSentFile) {
        this.failedSentFile = failedSentFile;
    }

    public String getDeliveredFile() {
        return deliveredFile;
    }

    public void setDeliveredFile(String deliveredFile) {
        this.deliveredFile = deliveredFile;
    }

    public String getFailedDeliveredFile() {
        return failedDeliveredFile;
    }

    public void setFailedDeliveredFile(String failedDeliveredFile) {
        this.failedDeliveredFile = failedDeliveredFile;
    }

}
