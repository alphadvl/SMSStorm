package com.scian.smsstorm.ui.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scian.smsstorm.R;

public class ClearableEditText extends RelativeLayout implements
        OnClickListener {

    private final int INVOKE_LISTENER_DELAY = 300;

    private final int HANDLER_MESSAGE_KEY = 1010;

    private final String CHANGED_TEXT = "CHANGED_TEXT";

    private Context mContext;

    private EditText mEditText;

    private ImageView mBtnClear;

    private View mParent;

    private Handler mHandler;

    private Bundle mBundle;

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mParent = LayoutInflater.from(mContext).inflate(
                R.layout.layout_clear_edit_text, this);
        mEditText = (EditText) mParent.findViewById(R.id.edit_text);
        mBtnClear = (ImageView) mParent.findViewById(R.id.edit_text_clear_btn);
        mEditText.addTextChangedListener(mWatcher);
        mBtnClear.setOnClickListener(this);
        mBtnClear.setEnabled(false);
        mEditText.setBackgroundDrawable(null);
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg != null) {
                    int what = msg.what;
                    if (what == HANDLER_MESSAGE_KEY) {
                        String text = msg.getData().getString(CHANGED_TEXT);
                        if (mOnTextChangedListener != null) {
                            mOnTextChangedListener.onTextChanged(text);
                        }
                    }
                }
            }
        };
        mBundle = new Bundle();
    }

    public CharSequence getText() {
        return mEditText.getText();
    }

    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    public void setEnable(boolean enable) {
        mEditText.setEnabled(enable);
        if (!enable) {
            mBtnClear.setEnabled(enable);
        }
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        mOnTextChangedListener = listener;
    }

    private OnTextChangedListener mOnTextChangedListener;

    private TextWatcher mWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            if (s.length() > 0) {
                mBtnClear.setEnabled(true);
            } else {
                mBtnClear.setEnabled(false);
            }
            mHandler.removeMessages(HANDLER_MESSAGE_KEY);
            Message msg = mHandler.obtainMessage();
            msg.what = HANDLER_MESSAGE_KEY;
            if (s.length() > 0) {
                mBundle.putString(CHANGED_TEXT, s.toString());
            } else {
                mBundle.putString(CHANGED_TEXT, "");
            }
            msg.setData(mBundle);
            mHandler.sendMessageDelayed(msg, INVOKE_LISTENER_DELAY);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public interface OnTextChangedListener {
        public void onTextChanged(CharSequence text);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnClear)) {
            mEditText.setText("");
            mBtnClear.setEnabled(false);
        }
    }

}
