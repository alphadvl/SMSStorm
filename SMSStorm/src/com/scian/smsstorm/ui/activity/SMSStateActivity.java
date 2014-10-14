package com.scian.smsstorm.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.scian.smsstorm.R;
import com.scian.smsstorm.ui.fragment.SmsStateFragment;

public class SMSStateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_sms_state);
        init();
    }

    private void init() {
        SmsStateFragment mFragment = new SmsStateFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.sms_state_fragment_container, mFragment);
        transaction.commit();
    }
}
