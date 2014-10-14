package com.scian.smsstorm.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class BaseListItem extends RelativeLayout {

    protected View mRootView;

    public BaseListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context);
    }

    public BaseListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    public BaseListItem(Context context) {
        super(context);
        inflate(context);
    }

    private void inflate(Context context) {
        mRootView = LayoutInflater.from(context)
                .inflate(getLayoutResId(), this);
        initView(mRootView);
    }

    protected abstract void initView(View view);

    protected abstract int getLayoutResId();

}
