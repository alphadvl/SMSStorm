package com.scian.smsstorm.ui.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.scian.smsstorm.R;
import com.scian.smsstorm.data.DataManger;
import com.scian.smsstorm.data.bean.Log;
import com.scian.smsstorm.ui.adapter.LogListAdapter;
import com.scian.smsstorm.ui.view.BackedButton;
import com.scian.smsstorm.util.FileUtil;
import com.scian.smsstorm.util.TestUtil;
import com.scian.smsstorm.util.TextUtil;

public class MainActivity extends Activity implements OnClickListener {

    private ImageView mBtnAdd;
    private BackedButton mBtnTest;
    private AsyncTask<Void, Void, List<Log>> mTask;
    private ListView mListView;
    private LogListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadLog();
    }

    private void initView() {
        mBtnAdd = (ImageView) findViewById(R.id.btn_main_add);
        mBtnTest = (BackedButton) findViewById(R.id.btn_add_testdata);
        mListView = (ListView) findViewById(R.id.log_list);
        mBtnTest.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mBtnAdd.setOnClickListener(this);
        mBtnTest.setOnClickListener(this);
        mAdapter = new LogListAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            gotoAdd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_main_add) {
            gotoAdd();
        } else if (id == R.id.btn_add_testdata) {
            String file = TestUtil.getAppendLineFile(this, 2000);
            if (TextUtil.isNullOrEmpty(file)) {
                Toast.makeText(this, "创建测试数据失败", 2000).show();
            } else {
                Toast.makeText(this, file, 2000).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RawDataBrowserActivity.class);
                intent.putExtra(RawDataBrowserActivity.RAW_DATA_FILE_PATH, file);
                startActivity(intent);
            }
        }
    }

    private void gotoAdd() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, RawDataLoadActivity.class);
        startActivity(intent);
    }

    private void loadLog() {
        mTask = new AsyncTask<Void, Void, List<Log>>() {

            @Override
            protected List<Log> doInBackground(Void... params) {
                List<Log> list = DataManger.getInstance().getHistory();
                return list;
            }

            @Override
            protected void onPostExecute(List<Log> result) {
                if (result != null && result.size() > 0) {
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter.SetData(result);
                    mBtnAdd.setVisibility(View.GONE);
                } else {
                    mListView.setVisibility(View.GONE);
                    mBtnAdd.setVisibility(View.VISIBLE);
                }
            }
        };

        mTask.execute();
    }

}
