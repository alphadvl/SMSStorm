package com.scian.smsstorm.ui.activity;

import java.util.List;

import com.scian.smsstorm.R;
import com.scian.smsstorm.constants.Config;
import com.scian.smsstorm.data.DataManger;
import com.scian.smsstorm.data.DataObservor;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.engine.EngineHandler;
import com.scian.smsstorm.interfaces.Listener.LoadRawDataListener;
import com.scian.smsstorm.ui.adapter.SearchListAdapter;
import com.scian.smsstorm.ui.view.BackedButton;
import com.scian.smsstorm.ui.view.SearchListView;
import com.scian.smsstorm.util.FileUtil;
import com.scian.smsstorm.util.TextUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class RawDataBrowserActivity extends FragmentActivity implements
        LoadRawDataListener, OnClickListener {

    public static final String RAW_DATA_FILE_PATH = "RAW_DATA_FILE_PATH";

    private SearchListView mListView;
    private BackedButton mBtnNext;
    private DataObservor mDataObservor;
    private SearchListAdapter mAdapter;
    private long mTag;
    private AsyncTask<String, Void, List<SearchItem>> mTask;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_raw_data_browser);
        initView();
        mAdapter = new SearchListAdapter(this);
        mListView.setAdapter(mAdapter);
        Intent input = getIntent();
        if (input != null) {
            String fileName = input.getStringExtra(RAW_DATA_FILE_PATH);
            mTag = input.getLongExtra(Config.KEY, -1);
            mBtnNext.setVisibility(View.GONE);
            if (!TextUtil.isNullOrEmpty(fileName)) {
                initTask();
                mTask.execute(fileName);
            }
        }
    }

    @Override
    protected void onStop() {
        if (mDataObservor != null) {
            mDataObservor.removeListener(this);
        }
        super.onStop();
    }

    private void initView() {
        mListView = (SearchListView) findViewById(R.id.raw_data_list);
        mBtnNext = (BackedButton) findViewById(R.id.btn_raw_data_start);
        mBtnNext.setVisibility(View.GONE);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onRawDataLoaded(final boolean succeed,
            final List<SearchItem> data) {
        mBtnNext.setVisibility(View.GONE);
        if (succeed) {
            if (data != null && data.size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setDataSource(data);
                        if (data.size() > 0) {
                            mBtnNext.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_raw_data_start) {
            List<SearchItem> source = mAdapter.getOriginSource();
            if (source != null && source.size() > 0) {
                DataManger.getInstance().setTempRawData(source);
                Intent intent = new Intent();
                intent.setClass(this, ContentSetActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void initTask() {
        mTask = new AsyncTask<String, Void, List<SearchItem>>() {
            @Override
            protected List<SearchItem> doInBackground(String... params) {
                if (params.length > 0) {
                    String file = params[0];
                    if (!TextUtil.isNullOrEmpty(file)) {
                        return FileUtil.readFromFile(file);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(final List<SearchItem> result) {
                mBtnNext.setVisibility(View.GONE);
                if (result != null && result.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setDataSource(result);
                            if (mTag <= 0) {
                                mBtnNext.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        };
    }
}
