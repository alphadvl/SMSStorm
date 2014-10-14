package com.scian.smsstorm.data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.engine.EngineHandler;
import com.scian.smsstorm.engine.Task;
import com.scian.smsstorm.interfaces.Listener;
import com.scian.smsstorm.interfaces.Listener.LoadRawDataListener;
import com.scian.smsstorm.interfaces.Listener.SMSDeliveredListener;
import com.scian.smsstorm.interfaces.Listener.SMSSendingListener;
import com.scian.smsstorm.interfaces.Listener.SMSSentListener;

public final class DataObservor {
    private ConcurrentHashMap<LoadRawDataListener, LoadRawDataListener> mRawDataLoadListenerList;
    private ConcurrentHashMap<SMSSendingListener, SMSSendingListener> mSMSSendingListenerList;
    private ConcurrentHashMap<SMSSentListener, SMSSentListener> mSMSSentListenerList;
    private ConcurrentHashMap<SMSDeliveredListener, SMSDeliveredListener> mSMSDeliveredListenerList;

    public DataObservor() {
        mRawDataLoadListenerList = new ConcurrentHashMap<LoadRawDataListener, LoadRawDataListener>();
        mSMSSendingListenerList = new ConcurrentHashMap<Listener.SMSSendingListener, Listener.SMSSendingListener>();
        mSMSSentListenerList = new ConcurrentHashMap<Listener.SMSSentListener, Listener.SMSSentListener>();
        mSMSDeliveredListenerList = new ConcurrentHashMap<Listener.SMSDeliveredListener, Listener.SMSDeliveredListener>();
    }

    public void addListener(LoadRawDataListener listener) {
        if (!mRawDataLoadListenerList.containsKey(listener)) {
            mRawDataLoadListenerList.put(listener, listener);
        }
    }

    public void addListener(SMSSendingListener listener) {
        if (!mSMSSendingListenerList.containsKey(listener)) {
            mSMSSendingListenerList.put(listener, listener);
        }
    }

    public void addListener(SMSSentListener listener) {
        if (!mSMSSentListenerList.containsKey(listener)) {
            mSMSSentListenerList.put(listener, listener);
        }
    }

    public void addListener(SMSDeliveredListener listener) {
        if (!mSMSDeliveredListenerList.containsKey(listener)) {
            mSMSDeliveredListenerList.put(listener, listener);
        }
    }

    public void removeListener(LoadRawDataListener listener) {
        mRawDataLoadListenerList.remove(listener);
    }

    public void removeListener(SMSSendingListener listener) {
        mSMSSendingListenerList.remove(listener);
    }

    public void removeListener(SMSSentListener listener) {
        mSMSSentListenerList.remove(listener);
    }

    public void removeListener(SMSDeliveredListener listener) {
        mSMSDeliveredListenerList.remove(listener);
    }

    public boolean exist(LoadRawDataListener listener) {
        return mRawDataLoadListenerList.containsKey(listener);
    }

    public boolean exist(SMSSendingListener listener) {
        return mSMSSendingListenerList.containsKey(listener);
    }

    public boolean exist(SMSSentListener listener) {
        return mSMSSentListenerList.containsKey(listener);
    }

    public boolean exist(SMSDeliveredListener listener) {
        return mSMSDeliveredListenerList.containsKey(listener);
    }

    public void dispatchRawDataList(final boolean succeed,
            final List<SearchItem> rawData) {
        Task task = new Task() {
            @Override
            public void run() {
                if (mRawDataLoadListenerList != null
                        && mRawDataLoadListenerList.size() > 0) {
                    for (LoadRawDataListener listener : mRawDataLoadListenerList
                            .values()) {
                        if (listener != null) {
                            listener.onRawDataLoaded(succeed, rawData);
                        }
                    }
                }
            }
        };
        EngineHandler.getaInstance().excute(task);
    }

    public void dispatchSending(final SearchItem item, final int count) {
        Task task = new Task() {
            @Override
            public void run() {
                if (mSMSSendingListenerList != null
                        && mSMSSendingListenerList.size() > 0) {
                    for (SMSSendingListener listener : mSMSSendingListenerList
                            .values()) {
                        if (listener != null) {
                            listener.onSending(item, count);
                        }
                    }
                }
            }
        };
        EngineHandler.getaInstance().excute(task);
    }

    public void dispatchSent(final boolean succeed, final SearchItem item,
            final int count) {
        Task task = new Task() {
            @Override
            public void run() {
                if (mSMSSentListenerList != null
                        && mSMSSentListenerList.size() > 0) {
                    for (SMSSentListener listener : mSMSSentListenerList
                            .values()) {
                        if (listener != null) {
                            listener.onSent(succeed, item, count);
                        }
                    }
                }
            }
        };
        EngineHandler.getaInstance().excute(task);
    }

    public void dispatchDelivered(final boolean succeed, final SearchItem item,
            final int count) {
        Task task = new Task() {
            @Override
            public void run() {
                if (mSMSDeliveredListenerList != null
                        && mSMSDeliveredListenerList.size() > 0) {
                    for (SMSDeliveredListener listener : mSMSDeliveredListenerList
                            .values()) {
                        if (listener != null) {
                            listener.onDelivered(succeed, item, count);
                        }
                    }
                }
            }
        };
        EngineHandler.getaInstance().excute(task);
    }

}
