package com.scian.smsstorm.ui.view;

import com.scian.smsstorm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class BackedButton extends Button {

    public BackedButton(Context context) {
        super(context);
        init();
    }

    public BackedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.button_background_selector));
    }

}
