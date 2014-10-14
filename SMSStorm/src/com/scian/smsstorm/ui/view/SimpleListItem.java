package com.scian.smsstorm.ui.view;

import com.scian.smsstorm.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SimpleListItem extends BaseListItem {

    private TextView mTextView;

    public SimpleListItem(Context context) {
        super(context);
    }

    @Override
    protected void initView(View view) {
        mTextView = (TextView) view.findViewById(R.id.list_item_number);
    }

    public TextView getTextView() {
        return mTextView;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_simple_list_item;
    }

}
