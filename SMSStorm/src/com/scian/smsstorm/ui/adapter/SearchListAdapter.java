package com.scian.smsstorm.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scian.smsstorm.data.bean.BaseItem;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.ui.view.ClearableEditText.OnTextChangedListener;
import com.scian.smsstorm.ui.view.SimpleListItem;
import com.scian.smsstorm.util.TextUtil;

public class SearchListAdapter extends BaseAdapter implements
        OnTextChangedListener {

    private AsyncTask<String, Void, List<SearchItem>> mAsyncTask;

    private int mLimitTextCount = 1;

    private boolean isSearchMode = false;

    private List<SearchItem> mDataSearch;

    private String mLastPattern;

    private List<SearchItem> mOriginData;

    private List<SearchItem> mDisplayData;

    private List<SearchItem> mLastSearchData;

    private HashMap<String, BaseItem> mDataMap;

    protected Context mContext;

    public SearchListAdapter(Context context) {
        mContext = context;
        mDisplayData = new ArrayList<SearchItem>();
        mOriginData = new ArrayList<SearchItem>();
        mLastSearchData = new ArrayList<SearchItem>();
        mDataSearch = new ArrayList<SearchItem>();
        mDataMap = new HashMap<String, BaseItem>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null) {
            convertView = new SimpleListItem(mContext);
            ViewHolder holder = new ViewHolder();
            holder.numnerTextView = ((SimpleListItem) convertView)
                    .getTextView();
            convertView.setTag(holder);
        }

        Object tag = convertView.getTag();
        if (tag instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) tag;
            SearchItem item = mDisplayData.get(position);
            String number = item.getNumber();
            if (item.getStartTextIndex() != item.getEndTextIndex()) {
                int start = item.getStartTextIndex();
                int end = item.getEndTextIndex();
                Spannable word = new SpannableString(number);
                word.setSpan(new AbsoluteSizeSpan(80), start, end,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                word.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                word.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.numnerTextView.setText(word);
            } else {
                holder.numnerTextView.setText(number);
            }

        }
        return convertView;
    }

    private void search(String input) {
        mAsyncTask = new AsyncTask<String, Void, List<SearchItem>>() {
            @Override
            protected List<SearchItem> doInBackground(String... params) {

                String pattern = null;
                if (params == null || params.length == 0) {
                    pattern = "";
                } else {
                    pattern = params[0];
                }

                if (TextUtil.isNullOrEmpty(pattern)) {
                    for (SearchItem item : mOriginData) {
                        item.refresh();
                    }
                    return mOriginData;
                }

                List<SearchItem> source = mOriginData;

                if (!TextUtil.isNullOrEmpty(mLastPattern)
                        && pattern.startsWith(mLastPattern)) {
                    source = mDataSearch;
                }

                mLastPattern = pattern;
                if (source == null) {
                    return new ArrayList<SearchItem>();
                }

                return TextUtil.matchString(pattern, source);
            }

            @Override
            protected void onPostExecute(List<SearchItem> result) {
                onSearchResult(result);
            }
        };

        mAsyncTask.execute(input);
    }

    private void onSearchResult(List<SearchItem> list) {
        mDataSearch = list;
        mDisplayData = list;
        notifyDataSetChanged();
    }

    private void init() {
        mDataMap.clear();
        mOriginData.clear();
        mDisplayData.clear();
        mLastSearchData.clear();
        mLastPattern = "";
    }

    public void setDataSource(List<SearchItem> list) {
        init();
        if (list != null && list.size() > 0) {
            for (SearchItem baseItem : list) {
                String key = baseItem.getNumber();
                if (!mDataMap.containsKey(key)) {
                    mDataMap.put(key, baseItem);
                    mOriginData.add(baseItem);
                    mDisplayData.add(baseItem);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void addData(SearchItem item) {
        String key = item.getNumber();
        if (!mDataMap.containsKey(key)) {
            mDataMap.put(key, item);
            mOriginData.add(item);
            if (isSearchMode) {
                search(mLastPattern);
            } else {
                mDisplayData.add(item);
                notifyDataSetChanged();
            }
        }
    }

    public void removeData(SearchItem item) {
        String key = item.getNumber();
        if (mDataMap.containsKey(key)) {
            BaseItem baseItem = mDataMap.get(key);
            mOriginData.remove(baseItem);
            mDisplayData.remove(baseItem);
            mLastSearchData.remove(baseItem);
            mDataMap.remove(key);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onTextChanged(CharSequence text) {
        isSearchMode = true;
        search(text.toString());
    }

    @Override
    public int getCount() {
        return mDisplayData.size();
    }

    @Override
    public Object getItem(int position) {
        return mDisplayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<SearchItem> getOriginSource() {
        return mOriginData;
    }

    public class ViewHolder {
        public TextView numnerTextView;
    }

}
