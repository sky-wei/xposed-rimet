/*
 * Copyright (c) 2020 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.rimet.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.MD5Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by starrysky on 16-4-19.
 */
public class FileUtil {
 
    public static final String TAG = "FileUtil";

    private static final Object lock = new Object();

    /**
     * 将日志写入文件。
     * @param message
     * @param tr
     */
    public static void writeLog(File logFile, String message, Throwable tr) {

        synchronized (lock) {

            FileWriter fileWriter = null;
            BufferedWriter bufdWriter = null;
            PrintWriter printWriter = null;

            try {
                File dir = logFile.getParentFile();
                if (!dir.exists()) dir.mkdir();

                fileWriter = new FileWriter(logFile, false);
                bufdWriter = new BufferedWriter(fileWriter);
                printWriter = new PrintWriter(fileWriter);

                // 添加头部信息
                bufdWriter.append(message);
                bufdWriter.flush();

                tr.printStackTrace(printWriter);
                printWriter.append("\n\n");
                printWriter.flush();
                fileWriter.flush();
            } catch (IOException e) {
                FileUtil.closeQuietly(fileWriter);
                FileUtil.closeQuietly(bufdWriter);
                FileUtil.closeQuietly(printWriter);
            }
        }
    }
 
    /**
     * 获取Assets目录下的文件内容(默认是UTF-8编码)
     * @param context
     * @param name
     * @return
     */
    public static String getAssetsContent(Context context, String name) {
 
        InputStream inputStream = null;
 
        AssetManager assetManager = context.getAssets();
 
        try {
            inputStream = assetManager.open(name);
 
            return new String(readContent(inputStream));
        } catch (Exception e) {
            Alog.e(TAG, "获取Assets数据异常!", e);
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }

    /**
     * 获取Assets目录下的图片
     * @param context
     * @param name
     * @return
     */
    public static Bitmap getAssetsImage(Context context, String name) {

        InputStream inputStream = null;

        AssetManager assetManager = context.getAssets();

        try {
            inputStream = assetManager.open(name);

            // 获取图标
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Alog.e(TAG, "获取Assets数据异常!", e);
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }
 
    /**
     * 读取指定文件中的内容并以byte数组形式的返回文件内容
     * @param source 读取的文件路径
     * @return 读取的文件内容
     */
    public static byte[] readFile(File source) {
 
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
 
        try {
            in = new FileInputStream(source);

            // 缓冲大小使用文件大小
            out = new ByteArrayOutputStream((int) source.length());
 
            int len;
            byte[] buffer = new byte[4096];
 
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
 
            return out.toByteArray();
        } catch (Exception e) {
            Alog.e(TAG, "获取文件信息异常", e);
        } finally {
            closeQuietly(out);
            closeQuietly(in);
        }
        return null;
    }
 
    /**
     * 把指定的byte数组内容保存到指定路径的文件中
     * @param target 保存的文件路径
     * @param content 需要保存的内容
     * @throws IOException
     */
    public static boolean writeFile(File target, byte[] content) {

        if (target == null || content == null) return false;
 
        File parent = target.getParentFile();
        if (!parent.isDirectory()) parent.mkdirs();
 
        FileOutputStream out = null;
 
        try {
            out = new FileOutputStream(target);
            out.write(content);
            out.flush();

            return true;
        }  catch (Exception e) {
            Alog.e(TAG, "写文件信息异常", e);
        } finally {
            closeQuietly(out);
        }
        return false;
    }
 
    /**
     * 把指定的内容保存到指定路径的文件中
     * @param target 保存的文件路径
     * @param content 需要保存的内容
     */
    public static boolean writeFile(File target, String content, String charsetName) {

        if (TextUtils.isEmpty(content)) return false;

        if (TextUtils.isEmpty(charsetName)) charsetName = "UTF-8";

        try {
            // 保存信息到本地
            return writeFile(target, content.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            Alog.e(TAG, "文本编码异常", e);
        }
        return false;
    }

    public static boolean copyFile(File source, File target) {

        if (source == null
                || !source.exists()
                || target == null) return false;

        File parent = target.getParentFile();
        if (!parent.isDirectory()) parent.mkdirs();

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            int length;
            byte[] buffer = new byte[2048];

            in = new FileInputStream(source);
            out = new FileOutputStream(target);

            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return true;
        }  catch (Exception e) {
            Alog.e(TAG, "复制文件信息异常", e);
        } finally {
            closeQuietly(out);
            closeQuietly(in);
        }
        return false;
    }

    /**
     * 把指定的内容保存到指定路径的文件中(默认UTF-8类型)
     * @param target 保存的文件路径
     * @param content 需要保存的内容
     */
    public static boolean writeFile(File target, String content) {
        // 保存信息到本地
        return writeFile(target, content, "UTF-8");
    }
 
    /**
     * 读取文件内容(默认的编码方式UTF-8)
     * @param file
     * @return
     */
    public static String readContent(File file) {
        return readContent(file, "UTF-8");
    }

    public static String readContent(File file, String charsetName) {

        if (file == null || !file.isFile()) return null;

        if (TextUtils.isEmpty(charsetName)) charsetName = "UTF-8";

        try {
            return new String(readFile(file), charsetName);
        } catch (Exception e) {
            Alog.e(TAG, "读取文件数据异常!", e);
        }
        return null;
    }

    public static List<String> readContentToList(File source) {

        if (source == null || !source.isFile()) return null;

        FileInputStream in = null;
        BufferedReader br = null;

        List<String> lines = new ArrayList<>();

        try {
            in = new FileInputStream(source);
            br = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (Exception e) {
            Alog.e(TAG, "获取文件信息异常", e);
        } finally {
            closeQuietly(br);
            closeQuietly(in);
        }
        return null;
    }
 
    public static byte[] readContent(InputStream in) throws IOException {
 
        ByteArrayOutputStream baos = null;
 
        try {
            int len;
            byte[] buffer = new byte[1024];
 
            baos = new ByteArrayOutputStream(1024);
 
            while ((len = in.read(buffer)) != -1) {
 
                baos.write(buffer, 0, len);
            }
 
            return baos.toByteArray();
        } finally {
            if (baos != null) baos.close();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }
 
    public static void delete(File file) {
 
        if (file == null || !file.exists()) return ;
 
        try {
            // 删除文件
            file.delete();
        } catch (Exception e) {
            Alog.e(TAG, "删除文件失败");
        }
    }

    public static void deleteDir(File dir) {

        if (dir == null || !dir.exists()) return ;

        File[] files = dir.listFiles();

        if (files == null) return ;

        for (File file : files) {
            // 删除文件
            delete(file);
        }

        // 删除目录
        delete(dir);
    }

    /**
     * 创建文件
     * @param file
     * @return
     */
    public static boolean createFile(File file) {

        if (file == null) return false;

        if (file.isFile()) return true;

        // 创建目录
        createDir(file.getParentFile());

        try {
            return file.createNewFile();
        } catch (IOException e) {
            Alog.e("CreateNewFile Exception", e);
        }
        return false;
    }

    /**
     * 创建文件目录
     * @param file
     * @return
     */
    public static boolean createDir(File file) {
        return file != null && file.mkdirs();
    }
 
    public static boolean exists(File file) {
        return file != null && file.exists();
    }
 
    public static boolean exists(String dir, String name) {
        return exists(new File(dir, name));
    }
 
    private static final DecimalFormat DF = new DecimalFormat("0.00");
 
    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "MB/" + DF.format((float) total / (1024 * 1024)) + "MB";
    }

    /**
     * 检查文件时候存在,不存在就创建
     * @param file
     */
    public static void checkOrCreateEmptyFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            Alog.e("获取md5异常", e);
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ignored) {}
        }
        return MD5Util.bytesToHexString(digest.digest());
    }
}