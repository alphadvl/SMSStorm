package com.scian.smsstorm.ui.activity;

import java.util.List;

import com.scian.smsstorm.R;
import com.scian.smsstorm.constants.Config;
import com.scian.smsstorm.constants.Data;
import com.scian.smsstorm.data.DataManger;
import com.scian.smsstorm.data.PreferenceManager;
import com.scian.smsstorm.data.bean.Log;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.service.SendServiceBinder;
import com.scian.smsstorm.service.SmsSendService;
import com.scian.smsstorm.ui.view.BackedButton;
import com.scian.smsstorm.ui.view.BackedEditText;
import com.scian.smsstorm.util.TextUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ContentSetActivity extends Activity implements OnClickListener {

    private BackedEditText mEditText;
    private BackedButton mBtnContent;
    private long mHistoryTag;

    protected ServiceConnection mServiceConnection;
    protected SendServiceBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_content);
        initView();
        bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mEditText = (BackedEditText) findViewById(R.id.edit_set_content);
        mBtnContent = (BackedButton) findViewById(R.id.btn_set_content_done);
        mBtnContent.setOnClickListener(this);
        mBtnContent.setVisibility(View.GONE);
    }

    private void initData() {
        mHistoryTag = getIntent().getLongExtra(Config.BUNDLE_KEY_TAG, 0);
        String content = "";
        if (mHistoryTag > 0) {
            mLogTask.execute();
        } else {
            content = PreferenceManager.getLatestSMSContent();
        }

        if (!TextUtil.isNullOrEmpty(content)) {
            mEditText.setText(content);
            mEditText.setSelection(content.length());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_set_content_done: {
                startService();
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        String content = mEditText.getText().toString();
        if (!TextUtil.isNullOrEmpty(content)) {
            PreferenceManager.setLatestSMSContent(content);
        }
        super.onDestroy();
    }

    public void check(final String content) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("短信确认");
        dialogBuilder.setMessage("确定要要群发短信吗？");
        dialogBuilder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start(content);
                    }
                });
        dialogBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        dialogBuilder.show();

    }

    private void startService() {
        String content = mEditText.getText().toString();
        if (!TextUtil.isNullOrEmpty(content)) {
            if (mBinder == null) {
                Toast.makeText(this, "服务启动失败!", 2000);
            } else {
                check(content);
            }
        } else {
            Toast.makeText(this, "请设置短信内容", 2000);
        }
    }

    private void start(String content) {
        long tag = System.currentTimeMillis();
        List<SearchItem> source = DataManger.getInstance().getTempRawData();
        mBinder.start(tag, content, source);
        Intent intent = new Intent();
        intent.setClass(this, SMSStateActivity.class);
        intent.putExtra(Config.KEY, tag);
        startActivity(intent);
        finish();
    }

    protected void bindService() {
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBinder = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (service instanceof SendServiceBinder) {
                    mBinder = (SendServiceBinder) service;
                    mBtnContent.setVisibility(View.VISIBLE);
                }
            }
        };
        Intent intent = new Intent();
        intent.setClass(this, SmsSendService.class);
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private AsyncTask<Void, Void, Log> mLogTask = new AsyncTask<Void, Void, Log>() {

        protected void onPostExecute(Log result) {
            if (result != null) {
                String content = result.getContent();
                if (TextUtil.isNullOrEmpty(content)) {
                    mEditText.setText(content);
                }
            }
        }

        @Override
        protected Log doInBackground(Void... params) {
            return DataManger.getInstance().getLogByTag(mHistoryTag);
        };
    };

}
