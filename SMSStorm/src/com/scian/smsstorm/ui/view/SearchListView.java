package com.scian.smsstorm.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.scian.smsstorm.R;
import com.scian.smsstorm.ui.adapter.SearchListAdapter;

public class SearchListView extends RelativeLayout {

    private ListView mListView;

    private SearchListAdapter mAdapter;

    private ClearableEditText mSearchText;

    private View mParent;

    private Context mContext;

    public SearchListView(Context context) {
        super(context);
        init(context);
    }

    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mParent = LayoutInflater.from(mContext).inflate(
                R.layout.layout_search_list, this);
        mSearchText = (ClearableEditText) mParent
                .findViewById(R.id.search_list_text);
        mListView = (ListView) mParent.findViewById(R.id.search_list_view);
    }

    public void setAdapter(SearchListAdapter adapter) {
        mAdapter = adapter;
        if (adapter != null) {
            mSearchText.setOnTextChangedListener(mAdapter);
            mListView.setAdapter(mAdapter);
        }
    }
}
