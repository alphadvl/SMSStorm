package com.scian.smsstorm.ui.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.scian.smsstorm.R;
import com.scian.smsstorm.ui.activity.RawDataBrowserActivity;
import com.scian.smsstorm.ui.view.BackedButton;
import com.scian.smsstorm.ui.view.BackedEditText;
import com.scian.smsstorm.util.TextUtil;

public class FileLoadFragment extends BaseFragment implements OnClickListener {

    private BackedEditText mEditText;
    private BackedButton mBtnLoadFile;
    private BackedButton mStart;
    private String mFilePath = "";
    private final int FILE_SELECT_CODE = 1110;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_load_file;
    }

    @Override
    protected void initView(View parent) {
        mEditText = (BackedEditText) parent.findViewById(R.id.edit_load_file);
        mBtnLoadFile = (BackedButton) parent.findViewById(R.id.btn_load_file);
        mStart = (BackedButton) parent.findViewById(R.id.btn_load_file_start);
        mBtnLoadFile.setOnClickListener(this);
        mStart.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected boolean shouldBindService() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_load_file) {
            showFileChooser();
        } else if (id == R.id.btn_load_file_start) {
            gotoDataBrowser();
        }
    }

    private void gotoDataBrowser() {
        if (!TextUtil.isNullOrEmpty(mFilePath)) {
            File file = new File(mFilePath);
            if (file.exists()) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RawDataBrowserActivity.class);
                intent.putExtra(RawDataBrowserActivity.RAW_DATA_FILE_PATH,
                        mFilePath);
                startActivity(intent);
                getActivity().finish();
                return;
            }
        }

        Toast.makeText(getActivity(), "文件不存在", 2000).show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个文件"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "请安装文件管理器", 2000).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                if (uri != null) {
                    File file = new File(uri.getPath());
                    String path = file.getAbsolutePath();
                    mEditText.setText(path);
                    mFilePath = path;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
