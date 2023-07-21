package com.zn.facealivelib.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static void copyFromAsset(Context context, String fileName, File dst, boolean overwrite) {
        if (!dst.exists() || overwrite) {
            try {
                //noinspection ResultOfMethodCallIgnored
                dst.createNewFile();
                InputStream in = context.getAssets().open(fileName);
                FileUtils.copyInStreamToFile(in, dst);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyInStreamToFile(InputStream in, File dst) throws IOException {
        FileOutputStream out = new FileOutputStream(dst);
        copyFile(in, out);
        in.close();
        out.flush();
        out.close();
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
