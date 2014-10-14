package com.scian.smsstorm.data;

import com.scian.smsstorm.SMSApplication;
import com.scian.smsstorm.constants.PreferenceKey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {

    public static void setLatestSMSContent(String content) {
        setString(PreferenceKey.KEY_LATEST_CONTENT, content);
    }

    public static String getLatestSMSContent() {
        return getString(PreferenceKey.KEY_LATEST_CONTENT);
    }

    public static void setLatestCustomFile(String path) {
        setString(PreferenceKey.KEY_LATEST_CUSTOM_FILE, path);
    }

    public static String getLatestCustomFile() {
        return getString(PreferenceKey.KEY_LATEST_CUSTOM_FILE);
    }

    public static String getHistoyTagFile() {
        return getString(PreferenceKey.KEY_HISTORY_TAG_FILE);
    }

    public static void setHistoryTagFile(String file) {
        setString(PreferenceKey.KEY_HISTORY_TAG_FILE, file);
    }

    private static String getString(String key) {
        String result = "";
        SharedPreferences store = getPreference();
        if (store != null) {
            result = store.getString(key, "");
        }

        return result;
    }

    private static void setString(String key, String content) {
        SharedPreferences store = getPreference();
        if (store != null) {
            Editor editor = store.edit();
            editor.putString(key, content);
            editor.commit();
        }
    }

    private static SharedPreferences getPreference() {
        return SMSApplication.getAppContext().getSharedPreferences(
                PreferenceKey.PREFERENCE_FILE_LOG, Context.MODE_PRIVATE);
    }

}
