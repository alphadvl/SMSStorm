package com.scian.smsstorm.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;

import com.scian.smsstorm.data.bean.SearchItem;

public final class FileUtil {

    public static void appendFileByLine(String fileName, String line) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        if (TextUtils.isEmpty(line))
            return;
        try {

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void appendFileByLine(String fileName, List<String> list) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(writer);
            for (String line : list) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void readFileByLine(String fileName, List<String> out) {
        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String temp = null;
                if (out == null) {
                    out = new ArrayList<String>();
                }
                while ((temp = reader.readLine()) != null) {
                    if (!TextUtils.isEmpty(temp)) {
                        temp = temp.replace(" ", "");
                        temp = temp.replace("-", "");
                        temp = temp.replace("+", "");
                        out.add(temp);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }

    @SuppressWarnings("finally")
    public static List<SearchItem> readFromFile(String fileName) {

        List<SearchItem> result = new ArrayList<SearchItem>();

        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader reader = null;
            HashMap<String, String> uniqueCache = new HashMap<String, String>();
            try {
                reader = new BufferedReader(new FileReader(file));
                String temp = null;
                while ((temp = reader.readLine()) != null) {
                    if (!TextUtils.isEmpty(temp)) {
                        temp = temp.replace(" ", "");
                        temp = temp.replace("-", "");
                        temp = temp.replace("+", "");
                        if (!uniqueCache.containsKey(temp)) {
                            uniqueCache.put(temp, temp);
                            SearchItem item = new SearchItem(temp);
                            result.add(item);
                        }
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (uniqueCache.size() > 0) {
                    uniqueCache.clear();
                    uniqueCache = null;
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }

                return result;
            }
        }
        return result;
    }

    public static void writeFileByLine(String fileName, List<String> list) {
        BufferedWriter writer = null;
        if (list.size() == 0)
            return;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            for (String number : list) {
                writer.write(number);
                writer.newLine();
            }
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean writeFileByLine(String fileName, List<SearchItem> list) {
        BufferedWriter writer = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            for (SearchItem item : list) {
                writer.write(item.getNumber());
                writer.newLine();
            }
            writer.flush();
            writer.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return false;
        }
    }

    public static String readFile(String path) {
        String result = "";
        InputStreamReader reader = null;
        try {

            File file = new File(path);
            if (!file.exists()) {
                return result;
            }
            reader = new InputStreamReader(new FileInputStream(file));
            List<Character> list = new ArrayList<Character>();
            int ch;
            if ((ch = reader.read()) != -1) {
                list.add((char) ch);
            }

            int length = list.size();
            if (length > 0) {
                char[] buffer = new char[length];
                for (int i = 0; i < length; i++) {
                    buffer[i] = list.get(i);
                }

                result = buffer.toString();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean writeFile(String path, String content) {
        boolean result = false;

        if (TextUtil.isNullOrEmpty(content)) {
            return true;
        }

        File file = new File(path);
        BufferedOutputStream bufferedOutputStream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] data = content.getBytes();

            bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(file));
            bufferedOutputStream.write(data);
            bufferedOutputStream.close();
            result = true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
