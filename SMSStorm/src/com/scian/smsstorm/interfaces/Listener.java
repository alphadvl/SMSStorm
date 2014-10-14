package com.scian.smsstorm.interfaces;

import java.util.List;

import android.R.integer;

import com.scian.smsstorm.data.bean.BaseItem;
import com.scian.smsstorm.data.bean.SearchItem;

public final class Listener {

    public interface LoadRawDataListener {
        public void onRawDataLoaded(boolean succeed, List<SearchItem> data);
    }

    public interface SMSSendingListener {
        public void onSending(SearchItem item, int count);
    }

    public interface SMSSentListener {
        public void onSent(boolean succeed, SearchItem item, int count);
    }

    public interface SMSDeliveredListener {
        public void onDelivered(boolean succeed, SearchItem item, int count);
    }

}
