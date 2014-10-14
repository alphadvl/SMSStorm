package com.scian.smsstorm.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import com.scian.smsstorm.data.FileManager;

import android.content.Context;

public class TestUtil {

    public static String getAppendLineFile(Context context, int count) {

        String path = FileManager.createTestRawDataFile(context);
        File file = new File(path);

        try {
            if (file.exists()) {
                return path;
            } else {
                file.createNewFile();
            }

            long base = 14012340000L;

            List<String> numbers = new ArrayList<String>();

            for (int i = 0; i < count; i++) {
                numbers.add(String.valueOf(++base));
            }

            if (numbers.size() > 0) {
                FileUtil.writeFileByLine(path, numbers);
            }

            return path;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }


}
