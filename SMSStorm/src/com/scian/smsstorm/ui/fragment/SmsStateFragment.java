package com.scian.smsstorm.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.scian.smsstorm.R;
import com.scian.smsstorm.constants.Config;
import com.scian.smsstorm.data.DataManger;
import com.scian.smsstorm.data.DataObservor;
import com.scian.smsstorm.data.bean.Log;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.interfaces.Listener.SMSDeliveredListener;
import com.scian.smsstorm.interfaces.Listener.SMSSendingListener;
import com.scian.smsstorm.interfaces.Listener.SMSSentListener;
import com.scian.smsstorm.ui.activity.SMSSendListActivity;
import com.scian.smsstorm.util.TextUtil;

public class SmsStateFragment extends BaseFragment implements OnClickListener,
        SMSDeliveredListener, SMSSendingListener, SMSSentListener {

    private static final String SMS_STATE_TAG = "SMS_STATE_TAG";

    private long mTag = -1;

    private View mAllSentLayout;
    private View mSentLayout;
    private View mFailedSentLayout;
    private View mDeliveredLayout;
    private View mFailedDeliveredLayout;
    private View mTimeLayout;
    private View mContentLayout;

    private TextView mTimeText;
    private TextView mContentText;
    private TextView mAllSentText;
    private TextView mSentText;
    private TextView mFailedSentText;
    private TextView mDeliveredText;
    private TextView mFailedDeliveredText;

    private Log mLog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sms_state;
    }

    @Override
    protected void initView(View parent) {
        mAllSentLayout = parent.findViewById(R.id.state_all_sent_layout);
        mSentLayout = parent.findViewById(R.id.state_sent_layout);
        mFailedSentLayout = parent.findViewById(R.id.state_sent_failed_layout);
        mDeliveredLayout = parent.findViewById(R.id.state_delivered_layout);
        mFailedDeliveredLayout = parent
                .findViewById(R.id.state_delivered_failed_layout);
        mContentLayout = parent.findViewById(R.id.state_content_layout);
        mTimeLayout = parent.findViewById(R.id.state_time_layout);

        mTimeText = (TextView) parent.findViewById(R.id.state_time_value);
        mContentText = (TextView) parent.findViewById(R.id.state_content_value);
        mAllSentText = (TextView) parent
                .findViewById(R.id.state_all_sent_value);
        mSentText = (TextView) parent.findViewById(R.id.state_sent_value);
        mFailedSentText = (TextView) parent
                .findViewById(R.id.state_sent_failed_value);
        mDeliveredText = (TextView) parent
                .findViewById(R.id.state_delivered_value);
        mFailedDeliveredText = (TextView) parent
                .findViewById(R.id.state_delivered_failed_value);

        mAllSentLayout.setOnClickListener(this);
        mSentLayout.setOnClickListener(this);
        mFailedDeliveredLayout.setOnClickListener(this);
        mFailedSentLayout.setOnClickListener(this);
        mDeliveredLayout.setOnClickListener(this);
        mContentLayout.setOnClickListener(this);
        mTimeLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTag = getActivity().getIntent().getLongExtra(Config.KEY, -1);

        if (mTag > 0) {
            DataObservor observor = DataManger.getInstance().getDataObservor(
                    mTag);
            if (observor != null) {
                observor.addListener((SMSDeliveredListener) this);
                observor.addListener((SMSSendingListener) this);
                observor.addListener((SMSSentListener) this);
            }

            mLog = DataManger.getInstance().getLogByTag(mTag);
            if (mLog != null) {
                initState(mLog);
            }
        }
    }

    private void initState(Log log) {
        loadTime(mTag);
        mContentText.setText(log.getContent());
        mAllSentText.setText(String.valueOf(log.getTotal()));
        mSentText.setText(String.valueOf(log.getSentCount()));
        mFailedSentText.setText(String.valueOf(log.getFailedSentCount()));
        mDeliveredText.setText(String.valueOf(log.getDeliveredCount()));
        mFailedDeliveredText.setText(String.valueOf(log
                .getFailedDeliveredCount()));
    }

    private void loadTime(long time) {
        mTimeText.setText(TextUtil.longToTimeString(time));
    }

    @Override
    protected boolean shouldBindService() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.state_all_sent_layout: {
                gotoDetail(Config.LIST_TYPE_ALL);
                break;
            }
            case R.id.state_sent_layout: {
                gotoDetail(Config.LIST_TYPE_SENT);
                break;
            }
            case R.id.state_sent_failed_layout: {
                gotoDetail(Config.LIST_TYPE_SENT_FAILED);
                break;
            }
            case R.id.state_delivered_layout: {
                gotoDetail(Config.LIST_TYPE_DELIVERED);
                break;
            }
            case R.id.state_delivered_failed_layout: {
                gotoDetail(Config.LIST_TYPE_DELIVERED_FAILED);
                break;
            }
            default: {
                break;
            }

        }
    }

    private void gotoDetail(int type) {
        if (mTag > 0) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SMSSendListActivity.class);
            intent.putExtra(Config.KEY, mTag);
            intent.putExtra(Config.TYPE, type);
            startActivity(intent);
        }
    }

    @Override
    public void onSent(final boolean succeed, final SearchItem item,
            final int count) {
        runOnUIThread(new Runnable() {

            @Override
            public void run() {
                if (succeed) {
                    mSentText.setText(String.valueOf(count));
                } else {
                    mFailedSentText.setText(String.valueOf(count));
                }
            }
        });
    }

    @Override
    public void onSending(final SearchItem item, final int count) {
        runOnUIThread(new Runnable() {

            @Override
            public void run() {
                mAllSentText.setText(String.valueOf(count));
            }
        });
    }

    @Override
    public void onDelivered(final boolean succeed, final SearchItem item,
            final int count) {
        runOnUIThread(new Runnable() {

            @Override
            public void run() {
                if (succeed) {
                    mDeliveredText.setText(String.valueOf(count));
                } else {
                    mFailedDeliveredText.setText(String.valueOf(count));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (mTag > 0) {
            DataObservor observor = DataManger.getInstance().getDataObservor(
                    mTag);
            if (observor != null) {
                observor.removeListener((SMSDeliveredListener) this);
                observor.removeListener((SMSSendingListener) this);
                observor.removeListener((SMSSentListener) this);
            }
        }
        super.onDestroyView();
    }
}
