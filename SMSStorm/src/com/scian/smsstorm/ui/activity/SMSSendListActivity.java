package com.scian.smsstorm.ui.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.scian.smsstorm.R;
import com.scian.smsstorm.constants.Config;
import com.scian.smsstorm.data.DataManger;
import com.scian.smsstorm.data.DataObservor;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.interfaces.Listener;
import com.scian.smsstorm.ui.adapter.SearchListAdapter;
import com.scian.smsstorm.ui.view.SearchListView;

public class SMSSendListActivity extends Activity {

    private SearchListView mListView;

    private SearchListAdapter mAdapter;

    private long mTag;

    private int mType;

    private List<SearchItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_list);
        mListView = (SearchListView) findViewById(R.id.sms_send_list);
        mAdapter = new SearchListAdapter(this);
        mListView.setAdapter(mAdapter);
        mTag = getIntent().getLongExtra(Config.KEY, -1);
        mType = getIntent().getIntExtra(Config.TYPE, 0);
        if (mTag > 0) {
            DataObservor observor = DataManger.getInstance().getDataObservor(
                    mTag);
            if (observor != null) {
                if (mType == Config.LIST_TYPE_SENT
                        || mType == Config.LIST_TYPE_SENT_FAILED) {
                    observor.addListener(mSentListener);
                } else if (mType == Config.LIST_TYPE_DELIVERED
                        || mType == Config.LIST_TYPE_DELIVERED_FAILED) {
                    observor.addListener(mSentListener);
                } else if (mType == Config.LIST_TYPE_SENDING) {
                    observor.addListener(mSendingListener);
                }
            }
            loadData();
        }

    }

    private void loadData() {
        if (mType == Config.LIST_TYPE_SENT_FAILED) {
            mData = DataManger.getInstance().getSentList(mTag, false);
        } else if (mType == Config.LIST_TYPE_DELIVERED) {
            mData = DataManger.getInstance().getDeliveredList(mTag, true);
        } else if (mType == Config.LIST_TYPE_DELIVERED_FAILED) {
            mData = DataManger.getInstance().getDeliveredList(mTag, false);
        } else if (mType == Config.LIST_TYPE_SENT) {
            mData = DataManger.getInstance().getSentList(mTag, true);
        } else if (mType == Config.LIST_TYPE_SENDING) {
            mData = DataManger.getInstance().getSendingList(mTag);
        } else {
            mData = DataManger.getInstance().getRawDataList(mTag);
        }
        mAdapter.setDataSource(mData);
    }

    private Listener.SMSSendingListener mSendingListener = new Listener.SMSSendingListener() {

        @Override
        public void onSending(SearchItem item, int count) {
            loadData();
        }
    };

    private Listener.SMSSentListener mSentListener = new Listener.SMSSentListener() {

        @Override
        public void onSent(boolean succeed, SearchItem item, int count) {
            loadData();
        }

    };

    private Listener.SMSDeliveredListener mDeliervedListener = new Listener.SMSDeliveredListener() {

        @Override
        public void onDelivered(boolean succeed, SearchItem item, int count) {
            loadData();
        }
    };

}
