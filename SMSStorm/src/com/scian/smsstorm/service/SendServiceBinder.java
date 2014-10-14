package com.scian.smsstorm.service;

import java.util.List;

import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.interfaces.ISMSServiceController;

import android.os.Binder;

public class SendServiceBinder extends Binder implements ISMSServiceController {

    ISMSServiceController mService;

    public SendServiceBinder(ISMSServiceController service) {
        mService = service;
    }

    @Override
    public void stop() {
        if (mService != null)
            mService.stop();
    }

    @Override
    public void start(long key, String content,List<SearchItem> list) {
        if (mService != null)
            mService.start(key,  content,list);
    }

    @Override
    public int getState() {
        if (mService != null)
            mService.getState();
        return -1;
    }
}
