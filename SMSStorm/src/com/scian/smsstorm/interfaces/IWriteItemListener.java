package com.scian.smsstorm.interfaces;

import com.scian.smsstorm.data.bean.BaseItem;

public interface IWriteItemListener {
    public void onWriteItem(BaseItem item);

    public void onWriteItem(String line);

    public void onWriteItemFailed();
}
