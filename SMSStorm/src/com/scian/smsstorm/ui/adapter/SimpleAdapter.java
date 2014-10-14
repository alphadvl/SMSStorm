package com.scian.smsstorm.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scian.smsstorm.data.bean.BaseItem;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.ui.view.ClearableEditText.OnTextChangedListener;
import com.scian.smsstorm.ui.view.SimpleListItem;

public class SimpleAdapter<T extends BaseItem> extends BaseAdapter {

    protected Context mContext;

    protected HashMap<String, T> mDataMap;

    protected List<T> mDataSource;

    public SimpleAdapter(Context context) {
        mContext = context;
        mDataMap = new HashMap<String, T>();
        mDataSource = new ArrayList<T>();
    }

    public void setDataSource(List<T> list) {
        mDataMap.clear();
        mDataSource.clear();
        if (list != null && list.size() > 0) {
            for (T baseItem : list) {
                String key = baseItem.getNumber();
                if (!mDataMap.containsKey(key)) {
                    mDataMap.put(key, baseItem);
                    mDataSource.add(baseItem);
                }
            }
        }

        notifyDataSetChanged();
    }
    
   

    public void addData(T item) {
        String key = item.getNumber();
        if (!mDataMap.containsKey(key)) {
            mDataMap.put(key, item);
            mDataSource.add(item);
            notifyDataSetChanged();
        }
    }

    public void removeData(T item) {
        String key = item.getNumber();
        if (mDataMap.containsKey(key)) {
            BaseItem baseItem = mDataMap.get(key);
            mDataSource.remove(baseItem);
            mDataMap.remove(key);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mDataSource.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        return null;
    }

}
