package com.scian.smsstorm.ui.view;

import com.scian.smsstorm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class BackedEditText extends EditText {

    public BackedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BackedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackedEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.comment_view_input_bg));
    }

}
