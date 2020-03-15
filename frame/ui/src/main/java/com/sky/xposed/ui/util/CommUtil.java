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

package com.sky.xposed.ui.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sky.xposed.common.util.Alog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sky on 2018/8/8.
 */
public class CommUtil {

    private CommUtil() {

    }

    public static void scanFile(Context context, String file) {
        Uri data = Uri.parse("file://" + file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    public static boolean saveImage2SDCard(String savePath, Bitmap bitmap) {

        try {
            File qrFile = new File(savePath);

            File parentFile = qrFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(qrFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            Alog.e("保存失败", e);
        }
        return false;
    }
}
