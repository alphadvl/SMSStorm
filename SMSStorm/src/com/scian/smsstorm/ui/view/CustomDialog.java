package com.scian.smsstorm.ui.view;

import com.scian.smsstorm.R;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context, R.style.simple_dialog_style);
    }

}
