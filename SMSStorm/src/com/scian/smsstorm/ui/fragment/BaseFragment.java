package com.scian.smsstorm.ui.fragment;

import com.scian.smsstorm.service.SendServiceBinder;
import com.scian.smsstorm.service.SmsSendService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    protected ServiceConnection mServiceConnection;
    protected SendServiceBinder mBinder;
    protected Handler mHandler;

    protected abstract int getLayoutId();

    protected abstract void initView(View parent);

    protected abstract void initData();

    protected abstract boolean shouldBindService();

    public boolean runOnUIThread(Runnable runnable) {
        if (mHandler != null) {
            return mHandler.post(runnable);
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler = new Handler();
        initView(view);
        initData();
        if (shouldBindService()) {
            bindService();
        }
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
                }
            }
        };
        Intent intent = new Intent();
        intent.setClass(getActivity(), SmsSendService.class);
        getActivity().bindService(intent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

}
