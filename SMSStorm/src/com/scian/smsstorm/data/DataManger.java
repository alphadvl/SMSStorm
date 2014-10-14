package com.scian.smsstorm.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.w3c.dom.ls.LSException;

import com.google.gson.Gson;
import com.scian.smsstorm.SMSApplication;
import com.scian.smsstorm.constants.Config;
import com.scian.smsstorm.constants.Data;
import com.scian.smsstorm.data.bean.BaseItem;
import com.scian.smsstorm.data.bean.HistoryTag;
import com.scian.smsstorm.data.bean.Log;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.util.FileUtil;
import com.scian.smsstorm.util.TextUtil;

public final class DataManger {

    private HashMap<Long, DataCache> mCacheData;

    private HashMap<Long, DataObservor> mObservor;

    private static DataManger sInstance;

    private List<SearchItem> mTempRawDataList;

    private HashMap<Long, Log> mRunningLog;

    private Gson mGson;

    private HashMap<Long, Log> mHistoryCache;

    private List<Log> mHistoryList;

    private boolean hasLoadHistory = false;

    static {
        sInstance = new DataManger();
    }

    public static DataManger getInstance() {
        return sInstance;
    }

    private DataManger() {
        init();
    }

    private void init() {
        mCacheData = new HashMap<Long, DataCache>();
        mGson = new Gson();
        mObservor = new HashMap<Long, DataObservor>();
        mHistoryCache = new HashMap<Long, Log>();
        mHistoryList = new ArrayList<Log>();
        mRunningLog = new HashMap<Long, Log>();
    }

    public DataObservor getDataObservor(long tag) {
        return mObservor.get(tag);
    }

    public ConcurrentHashMap<String, SearchItem> getSendingData(long tag) {
        DataCache cacheItem = mCacheData.get(tag);
        if (cacheItem != null) {
            return cacheItem.sendingData;
        }

        return null;
    }

    public void prepare(long key, String content) {
        if (!mRunningLog.containsKey(key)) {
            Log log = new Log();
            log.setTag(key);
            log.setContent(content);
            log.setTotalFile(FileManager.createFilePath(
                    Data.DATA_FILE_NAME_RAW, key));
            log.setDeliveredFile(FileManager.createFilePath(
                    Data.DATA_FILE_NAME_DELIERED, key));
            log.setFailedDeliveredFile(FileManager.createFilePath(
                    Data.DATA_FILE_NAME_DELIERED_FAILED, key));
            log.setSentFile(FileManager.createFilePath(Data.DATA_FILE_NAME_RAW,
                    key));
            log.setFailedSentFile(FileManager.createFilePath(
                    Data.DATA_FILE_NAME_SENT_FAILED, key));
            mRunningLog.put(key, log);
        }

        if (!mObservor.containsKey(key)) {
            DataObservor observor = new DataObservor();
            mObservor.put(key, observor);
        }
    }

    public boolean backupRawData(long key, List<SearchItem> source) {

        Log log = mRunningLog.get(key);
        if (log != null) {
            String path = FileManager.getLogFilePath(key);
            String logjson = mGson.toJson(log);
            FileUtil.writeFile(path, logjson);
            android.util.Log.i(Config.TAG, "write log file:" + path);
            mHistoryList.add(log);
            List<Long> list = new ArrayList<Long>();
            for (Log item : mHistoryList) {
                list.add(item.getTag());
            }
            HistoryTag history = new HistoryTag();
            history.setList(list);
            String historyPath = FileManager.getHistory();
            String historyJosn = mGson.toJson(history);
            FileUtil.writeFile(historyPath, historyJosn);
            android.util.Log.i(Config.TAG, "write history file:" + path);
        }

        String fileName = FileManager.createFilePath(
                SMSApplication.getAppContext(), Data.DATA_FILE_NAME_RAW, key);
        boolean succeed = FileUtil.writeFileByLine(fileName, source);
        if (succeed) {
            if (source.size() > 0) {
                DataCache cache = new DataCache();
                for (SearchItem searchItem : source) {
                    String number = searchItem.getNumber();
                    if (!cache.allSourceData.contains(number)) {
                        cache.allSourceData.put(number, searchItem);
                    }
                }
                mCacheData.put(key, cache);
            }
        }
        return succeed;
    }

    public String getSmsContent(long tag) {
        if (mRunningLog.containsKey(tag)) {
            Log log = mRunningLog.get(tag);
            if (log != null) {
                return log.getContent();
            }
        }

        return "";
    }

    public void onSendingItem(long tag, String number) {
        DataCache cacheItem = mCacheData.get(tag);
        if (cacheItem != null) {
            SearchItem item = cacheItem.allSourceData.get(number);
            if (item != null) {
                item.setState(BaseItem.SMS_STATE_SENDING);
                cacheItem.onSending(item);
                if (mObservor.containsKey(tag)) {
                    DataObservor observor = mObservor.get(tag);
                    observor.dispatchSending(item, cacheItem.sendingData.size());
                }
            }
        }
    }

    public void onSent(long tag, boolean succeed, String number) {

        DataCache cacheItem = mCacheData.get(tag);
        if (cacheItem != null) {
            SearchItem item = cacheItem.allSourceData.get(number);
            if (item != null) {
                item.setState(BaseItem.SMS_STATE_SENT);
                cacheItem.onSent(succeed, item);
                if (mObservor.containsKey(tag)) {
                    DataObservor observor = mObservor.get(tag);
                    if (succeed) {
                        observor.dispatchSent(succeed, item,
                                cacheItem.sentData.size());
                    } else {
                        observor.dispatchSent(succeed, item,
                                cacheItem.failedSentData.size());
                    }

                }
            }
        }
    }

    public List<SearchItem> getSendingList(long tag) {
        List<SearchItem> list = new ArrayList<SearchItem>();
        if (mCacheData.containsKey(tag)) {
            Collection<SearchItem> collection = mCacheData.get(tag).sendingData
                    .values();
            if (collection != null && collection.size() > 0) {
                list.addAll(collection);
            }
        } else {
            String path = FileManager.getFilePath(tag,
                    Data.DATA_FILE_NAME_SENDING);
            list = FileUtil.readFromFile(path);
        }
        return list;
    }

    public List<SearchItem> getRawDataList(long tag) {
        List<SearchItem> list = new ArrayList<SearchItem>();
        if (mCacheData.containsKey(tag)) {
            Collection<SearchItem> collection = mCacheData.get(tag).allSourceData
                    .values();
            if (collection != null && collection.size() > 0) {
                list.addAll(collection);
            }
        } else {
            String path = FileManager.getFilePath(tag, Data.DATA_FILE_NAME_RAW);
            list = FileUtil.readFromFile(path);
        }
        return list;
    }

    public List<SearchItem> getSentList(long tag, boolean succeed) {
        List<SearchItem> list = new ArrayList<SearchItem>();
        if (mCacheData.containsKey(tag)) {
            Collection<SearchItem> collection = null;
            if (succeed) {
                collection = mCacheData.get(tag).sentData.values();
            } else {
                collection = mCacheData.get(tag).failedSentData.values();
            }

            if (collection != null && collection.size() > 0) {
                list.addAll(collection);
            }

        } else {
            String path = FileManager.getFilePath(tag,
                    succeed ? Data.DATA_FILE_NAME_SENT
                            : Data.DATA_FILE_NAME_SENT_FAILED);
            list = FileUtil.readFromFile(path);
        }
        return list;
    }

    public List<SearchItem> getDeliveredList(long tag, boolean succeed) {
        List<SearchItem> list = new ArrayList<SearchItem>();
        if (mCacheData.containsKey(tag)) {
            Collection<SearchItem> collection = null;
            if (succeed) {
                collection = mCacheData.get(tag).deliveredData.values();
            } else {
                collection = mCacheData.get(tag).failedDeliveredData.values();
            }

            if (collection != null && collection.size() > 0) {
                list.addAll(collection);
            }

        } else {
            String path = FileManager.getFilePath(tag,
                    succeed ? Data.DATA_FILE_NAME_DELIERED
                            : Data.DATA_FILE_NAME_DELIERED_FAILED);
            list = FileUtil.readFromFile(path);
        }
        return list;
    }

    public void onDelivered(long tag, boolean succeed, String number) {
        DataCache cacheItem = mCacheData.get(tag);
        if (cacheItem != null) {
            SearchItem item = cacheItem.allSourceData.get(number);
            if (item != null) {
                item.setState(BaseItem.SMS_STATE_DELIVERED);
                cacheItem.onDelivered(succeed, item);
                if (mObservor.containsKey(tag)) {
                    DataObservor observor = mObservor.get(tag);
                    if (succeed) {
                        observor.dispatchDelivered(succeed, item,
                                cacheItem.deliveredData.size());
                    } else {
                        observor.dispatchDelivered(succeed, item,
                                cacheItem.failedDeliveredData.size());
                    }
                }
            }
        }
    }

    public void setTempRawData(List<SearchItem> list) {
        mTempRawDataList = list;
    }

    public List<SearchItem> getTempRawData() {
        List<SearchItem> list = new ArrayList<SearchItem>();
        if (mTempRawDataList != null && mTempRawDataList.size() > 0) {
            list.addAll(mTempRawDataList);
            mTempRawDataList.clear();
            mTempRawDataList = null;
        }
        return list;
    }

    public Log getLogByTag(long tag) {

        if (mRunningLog.containsKey(tag)) {
            return mRunningLog.get(tag);
        } else {

        }

        if (mHistoryCache.size() == 0 && !hasLoadHistory) {
            loadHistory();
        }

        if (mHistoryCache.containsKey(tag)) {
            Log log = mHistoryCache.get(tag);
            if (log == null) {
                String file = FileManager.getLogFilePath(tag);
                String json = FileUtil.readFile(file);
                if (!TextUtil.isNullOrEmpty(json)) {
                    log = mGson.fromJson(json, Log.class);
                    if (log != null) {
                        mHistoryCache.remove(tag);
                        mHistoryCache.put(tag, log);
                    }
                }
            }
            return log;
        } else {
            return null;
        }
    }

    private void loadHistory() {
        String fileName = FileManager.getHistory();
        String json = FileUtil.readFile(fileName);
        if (!TextUtil.isNullOrEmpty(json)) {
            try {
                HistoryTag history = mGson.fromJson(json, HistoryTag.class);
                if (history != null) {
                    List<Long> list = history.getList();
                    if (list != null && list.size() > 0) {
                        for (Long tag : list) {
                            String logfile = FileManager.getLogFilePath(tag);
                            String logString = FileUtil.readFile(logfile);
                            Log log = null;
                            try {
                                log = mGson.fromJson(logString, Log.class);
                                mHistoryList.add(log);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (!mHistoryCache.containsKey(tag)) {
                                mHistoryCache.put(tag, log);
                            }
                        }
                        hasLoadHistory = true;
                        list.clear();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Log> getHistory() {
        if (mHistoryList.size() == 0 && !hasLoadHistory) {
            loadHistory();
        }

        return mHistoryList;
    }
}
