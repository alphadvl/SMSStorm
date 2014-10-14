package com.scian.smsstorm.util;

import java.io.File;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Build.VERSION_CODES;

public final class AppUtil {

    public static String getTimeString() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append(month > 9 ? "" + month : "0" + month);
        builder.append(date > 9 ? "" + date : "0" + date);
        builder.append(hour > 9 ? "" + hour : "0" + hour);
        builder.append(min > 9 ? "" + min : "0" + min);
        builder.append(sec > 9 ? "" + sec : "0" + sec);
        return builder.toString();
    }

    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @TargetApi(VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.FROYO
                && context.getExternalCacheDir() != null) {
            return context.getExternalCacheDir();
        }

        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/data/";
        return new File(Environment.getExternalStorageDirectory().getPath()
                + cacheDir);
    }

}
