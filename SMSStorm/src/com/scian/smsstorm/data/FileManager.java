package com.scian.smsstorm.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.scian.smsstorm.SMSApplication;
import com.scian.smsstorm.constants.Data;
import com.scian.smsstorm.data.bean.SearchItem;
import com.scian.smsstorm.util.AppUtil;
import com.scian.smsstorm.util.FileUtil;
import com.scian.smsstorm.util.TextUtil;

public class FileManager {

    private Context mContext;

    private String mDefaultPath;

    public FileManager(Context context) {
        mContext = context;
    }

    public List<SearchItem> readRawData(String path) {
        List<SearchItem> result = new ArrayList<SearchItem>();

        List<String> list = new ArrayList<String>();
        FileUtil.readFileByLine(path, list);

        if (list.size() > 0) {
            for (String number : list) {
                if (!TextUtil.isNullOrEmpty(number)) {
                    SearchItem item = new SearchItem(number);
                    result.add(item);
                }
            }
        }
        return result;
    }

    public static String createTestRawDataFile(Context context) {
        int extention = (int) (System.currentTimeMillis() / 100000L);

        String fileName = "test_raw_data_line_" + extention + ".txt";
        String path = getFilePath(context);
        return path + File.separator + fileName;
    }

    public static String createFilePath(Context context, String prefix, long tag) {
        String fileName = prefix + tag + ".txt";
        final String path = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !AppUtil.isExternalStorageRemovable() ? AppUtil
                .getExternalCacheDir(context).getPath() : context.getCacheDir()
                .getPath();

        return path + File.separator + fileName;
    }

    public static String createFilePath(String prefix, long tag) {
        String fileName = prefix + tag + ".txt";
        String path = getFilePath(SMSApplication.getAppContext());
        return path + File.separator + fileName;
    }

    public static String getHistory() {
        String path = getFilePath(SMSApplication.getAppContext());
        return path + File.separator + Data.DATA_FILE_NAME_HISTORY;
    }

    public static String getLogFilePath(long tag) {
        String fileName = Data.DATA_FILE_NAME_LOG + tag + ".txt";
        String path = getFilePath(SMSApplication.getAppContext());
        return path + File.separator + fileName;
    }
    
    public static String getFilePath(long tag,String type){
        String fileName = type + tag + ".txt";
        String path = getFilePath(SMSApplication.getAppContext());
        return path + File.separator + fileName;
    }


    public static String getFilePath(Context context) {
        String path = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !AppUtil.isExternalStorageRemovable() ? AppUtil
                .getExternalCacheDir(context).getPath() : context.getCacheDir()
                .getPath();

        return path;
    }

}
