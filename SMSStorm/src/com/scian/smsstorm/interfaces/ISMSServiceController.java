package com.scian.smsstorm.interfaces;

import java.util.List;

import com.scian.smsstorm.data.bean.SearchItem;

public interface ISMSServiceController {
    public void stop();

    public void start(long key,String content, List<SearchItem> list);

    public int getState();
}
