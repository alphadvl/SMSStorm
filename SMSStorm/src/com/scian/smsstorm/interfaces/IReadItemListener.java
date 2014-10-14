package com.scian.smsstorm.interfaces;

import com.scian.smsstorm.data.bean.BaseItem;

public interface IReadItemListener {
    
    public void onReadItem(BaseItem item);

    public void onReadItem(String line);
}
