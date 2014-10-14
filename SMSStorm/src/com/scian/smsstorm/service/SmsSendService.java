package com.scian.smsstorm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.scian.smsstorm.constants.Config;
import com.scian.smsstorm.data.DataManger;
import com.scian.smsstorm.data.bean.BaseItem;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.engine.EngineHandler;
import com.scian.smsstorm.engine.Task;
import com.scian.smsstorm.interfaces.ISMSServiceController;
import com.scian.smsstorm.util.TextUtil;

public class SmsSendService extends Service implements ISMSServiceController {

    SendServiceBinder mBinder;

    private int mState = Config.SERVICE_STATE_IDLE;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new SendServiceBinder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void stop() {
    }

    @Override
    public void start(final long key, final String content,
            final List<SearchItem> list) {
        setState(Config.SERVICE_STATE_RUNNING);
        DataManger.getInstance().prepare(key, content);
        Task task = new Task() {
            @Override
            public void run() {
                if (DataManger.getInstance().backupRawData(key, list)) {
                    send(key);
                } else {
                    setState(Config.SERVICE_STATE_IDLE);
                }
            }
        };
        EngineHandler.getaInstance().excute(task);
    }

    @Override
    public synchronized int getState() {
        return mState;
    }

    private synchronized void setState(int state) {
        mState = state;
    }

    private void send(long key) {
        String content = DataManger.getInstance().getSmsContent(key);
        Log.i(Config.TAG, "[send]:content=" + content);
        if (!TextUtil.isNullOrEmpty(content)) {
            List<String> species = new ArrayList<String>();
            SmsManager smsManager = SmsManager.getDefault();
            if (content.length() > 70) {
                species = smsManager.divideMessage(content);
            } else {
                species.add(content);
            }

            Log.d(Config.TAG, "[send]:segment=" + species.size());
            ConcurrentHashMap<String, SearchItem> sendingList = DataManger
                    .getInstance().getSendingData(key);
            if (sendingList != null && sendingList.size() > 0) {
                try {
                    for (SearchItem item : sendingList.values()) {
                        try {
                            for (String c : species) {
                                // smsManager.sendTextMessage(number, null, c,
                                // getPendingIntent(Consts.SENT, number),
                                // getPendingIntent(Consts.DELIVERED, number));
                                DataManger.getInstance().onSendingItem(key,
                                        item.getNumber());
                            }
                            if (Config.SMS_SEND_SLEEP_TIME > 0) {
                                Thread.sleep(Config.SMS_SEND_SLEEP_TIME);
                            }
                        } catch (Exception e) {

                        }
                    }
                    setState(Config.SERVICE_STATE_IDLE);
                } catch (Exception e) {
                } finally {
                    setState(Config.SERVICE_STATE_IDLE);
                }
            }

        }

    }

    private BroadcastReceiver mSendReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = getResultCode();
            Log.d(Config.TAG, "[mSendReceiver]:resultCode=" + resultCode);
            switch (resultCode) {
                case Activity.RESULT_OK:
                    onSent(true, intent);
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    onSent(false, intent);
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    onSent(false, intent);
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    onSent(false, intent);
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    onSent(false, intent);
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver mDeliverReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = getResultCode();
            Log.d(Config.TAG, "[mDeliverReceiver]:resultCode=" + resultCode);
            switch (resultCode) {
                case Activity.RESULT_OK:
                    onDelivered(true, intent);
                    break;
                case Activity.RESULT_CANCELED:
                    onDelivered(false, intent);
                    break;
            }
        }
    };

    private PendingIntent getPendingIntent(String action, String number,
            long tag) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(Config.NUMBER, number);
        intent.putExtra(Config.KEY, tag);
        return PendingIntent.getBroadcast(SmsSendService.this, 0, intent, 0);
    }

    private void onSent(boolean succeed, Intent intent) {
        String number = intent.getStringExtra(Config.NUMBER);
        long tag = intent.getLongExtra(Config.KEY, 0);
        DataManger.getInstance().onSent(tag, succeed, number);
    }

    private void onDelivered(boolean succeed, Intent intent) {
        String number = intent.getStringExtra(Config.NUMBER);
        long tag = intent.getLongExtra(Config.KEY, 0);
        DataManger.getInstance().onDelivered(tag, succeed, number);
    }

}
