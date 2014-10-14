package com.scian.smsstorm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.text.TextUtils;

import com.scian.smsstorm.data.bean.BaseItem;
import com.scian.smsstorm.data.bean.SearchItem;

public final class TextUtil {

    public static boolean isNullOrEmpty(String text) {
        if (null == text) {
            return true;
        }

        if (TextUtils.isEmpty(text)) {
            return true;
        }

        return false;
    }

    public static List<SearchItem> matchString(String pattern,
            List<SearchItem> list) {
        char[] patternArray = pattern.toCharArray();
        int pattern_length = patternArray.length;
        HashMap<Integer, List<SearchItem>> mSortMap = new HashMap<Integer, List<SearchItem>>();
        for (SearchItem baseItem : list) {
            char[] sourceArray = baseItem.getNumber().toCharArray();
            int source_length = sourceArray.length;
            if (source_length >= pattern_length) {
                int index = KMPUtil.indexOf(sourceArray, patternArray);
                if (index >= 0) {
                    List<SearchItem> sublist = null;
                    if (!mSortMap.containsKey(index)) {
                        sublist = new ArrayList<SearchItem>();
                        mSortMap.put(index, sublist);
                    } else {
                        sublist = mSortMap.get(index);
                    }
                    baseItem.setStartTextIndex(index);
                    baseItem.setEndTextIndex(index + pattern_length);
                    sublist.add(baseItem);
                    continue;
                }
            }

            baseItem.refresh();
        }
        int grade = mSortMap.size();
        List<SearchItem> resultList = new ArrayList<SearchItem>();
        if (grade > 0) {
            int[] array = new int[grade];
            Iterator<Integer> iterator = mSortMap.keySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                array[i] = iterator.next();
                i++;
            }
            Arrays.sort(array);

            for (i = 0; i < grade; i++) {
                List<SearchItem> temp = mSortMap.get(array[i]);
                if (temp != null && temp.size() > 0)
                    resultList.addAll(temp);
            }
            mSortMap.clear();
        }

        return resultList;
    }

    public static String longToTimeString(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Formatter ft = new Formatter(Locale.CHINA);
        String timeString = ft.format("%1$tYƒÍ%1$tm‘¬%1$td»’%1$tA£¨%1$tT %1$tp",
                cal).toString();
        return timeString;
    }
}
