package com.seeta.sdk;

import android.util.Log;

/**
 * Created by mimi on 2018/5/29.
 */

public class ImageDataFormatUtils {

    static {
        Log.e("ImageDataFormatUtils", "Start Load");
        System.loadLibrary("ImageDataFormatUtils");
        Log.e("ImageDataFormatUtils", "Finish Load");
    }
    public static native  byte[] Nv21ToARGB(byte[] data, int width, int height);

    public static native byte[] ARGBToBGR(byte[] data, int width, int height);

    public static native byte[] Nv21ToBGR(byte[] data, int width, int height);
}
