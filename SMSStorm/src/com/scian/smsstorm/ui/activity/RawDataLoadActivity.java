package com.scian.smsstorm.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.scian.smsstorm.R;
import com.scian.smsstorm.ui.fragment.BaseFragment;
import com.scian.smsstorm.ui.fragment.FileLoadFragment;

public class RawDataLoadActivity extends FragmentActivity {

    private BaseFragment mFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_load_file);
        init();
    }

    private void init() {
        mFragment = new FileLoadFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.layout_load_file_container, mFragment);
        transaction.commit();
    }

}
