package com.scian.smsstorm.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.scian.smsstorm.constants.Data;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.engine.EngineHandler;
import com.scian.smsstorm.engine.Task;
import com.scian.smsstorm.util.FileUtil;
import com.scian.smsstorm.util.TextUtil;

public class DataCache {

    private static int MAX_CACHE_TEMP_SIZE = 20;

    public long tag;

    public ConcurrentHashMap<String, SearchItem> allSourceData;
    public ConcurrentHashMap<String, SearchItem> sendingData;
    public ConcurrentHashMap<String, SearchItem> failedSentData;
    public ConcurrentHashMap<String, SearchItem> failedDeliveredData;
    public ConcurrentHashMap<String, SearchItem> sentData;
    public ConcurrentHashMap<String, SearchItem> deliveredData;

    private List<String> tempSendingData;
    private List<String> tempSentData;
    private List<String> tempDeliveredData;
    private List<String> tempFailedSentData;
    private List<String> tempFailedDeliveredData;

    public DataCache() {
        allSourceData = new ConcurrentHashMap<String, SearchItem>();
        sendingData = new ConcurrentHashMap<String, SearchItem>();
        failedSentData = new ConcurrentHashMap<String, SearchItem>();
        failedDeliveredData = new ConcurrentHashMap<String, SearchItem>();
        sentData = new ConcurrentHashMap<String, SearchItem>();
        deliveredData = new ConcurrentHashMap<String, SearchItem>();
    }

    public void init(long tag, List<SearchItem> source) {
        this.tag = tag;
        if (source != null && source.size() > 0) {
            for (SearchItem searchItem : source) {
                String number = searchItem.getNumber();
                if (!allSourceData.containsKey(number)) {
                    allSourceData.put(number, searchItem);
                }
            }
        }
    }

    public void onSending(SearchItem item) {
        String number = item.getNumber();
        if (!sendingData.containsKey(number)) {
            sendingData.put(number, item);
        }
        tryFlush(item, tempSendingData, Data.DATA_FILE_NAME_SENDING);
    }

    public void onSent(boolean succeed, SearchItem item) {
        String number = item.getNumber();
        if (succeed) {
            if (!sentData.containsKey(number)) {
                sentData.put(number, item);
                tryFlush(item, tempSentData, Data.DATA_FILE_NAME_SENT);
            }
        } else {
            if (!failedSentData.containsKey(number)) {
                failedSentData.put(number, item);
                tryFlush(item, tempFailedSentData,
                        Data.DATA_FILE_NAME_SENT_FAILED);
            }
        }
    }

    public void onDelivered(boolean succeed, SearchItem item) {
        String number = item.getNumber();
        if (succeed) {
            if (!deliveredData.containsKey(number)) {
                deliveredData.putIfAbsent(number, item);
                tryFlush(item, tempSentData, Data.DATA_FILE_NAME_SENT);
            }
        } else {
            if (!failedDeliveredData.containsKey(number)) {
                failedDeliveredData.putIfAbsent(number, item);
                tryFlush(item, tempFailedSentData,
                        Data.DATA_FILE_NAME_SENT_FAILED);
            }
        }
    }

    public void flush() {

        if (tempDeliveredData.size() > 0) {
            Task task = createWriteFileTask(tempDeliveredData,
                    Data.DATA_FILE_NAME_DELIERED);
            EngineHandler.getaInstance().excute(task);
        }

        if (tempFailedDeliveredData.size() > 0) {
            Task task = createWriteFileTask(tempFailedDeliveredData,
                    Data.DATA_FILE_NAME_DELIERED_FAILED);
            EngineHandler.getaInstance().excute(task);
        }

        if (tempFailedSentData.size() > 0) {
            Task task = createWriteFileTask(tempFailedSentData,
                    Data.DATA_FILE_NAME_SENT_FAILED);
            EngineHandler.getaInstance().excute(task);
        }

        if (tempSendingData.size() > 0) {
            Task task = createWriteFileTask(tempSendingData,
                    Data.DATA_FILE_NAME_SENDING);
            EngineHandler.getaInstance().excute(task);
        }

        if (tempSentData.size() > 0) {
            Task task = createWriteFileTask(tempSentData,
                    Data.DATA_FILE_NAME_SENT);
            EngineHandler.getaInstance().excute(task);
        }

    }

    private void tryFlush(SearchItem item, List<String> tempcache, String type) {
        if (tempcache == null) {
            tempcache = new ArrayList<String>();
        }

        String number = item.getNumber();
        if (!TextUtil.isNullOrEmpty(number)) {
            tempcache.add(number);
        }

        if (tempcache.size() >= MAX_CACHE_TEMP_SIZE) {
            final List<String> temp = new ArrayList<String>();
            temp.addAll(tempcache);
            tempcache.clear();
            Task task = createWriteFileTask(temp, type);
            EngineHandler.getaInstance().excute(task);
        }
    }

    private Task createWriteFileTask(final List<String> list, final String type) {
        Task task = new Task() {

            @Override
            public void run() {
                String fileName = FileManager.getFilePath(DataCache.this.tag,
                        type);
                FileUtil.appendFileByLine(fileName, list);
                list.clear();
            }
        };

        return task;
    }

}
