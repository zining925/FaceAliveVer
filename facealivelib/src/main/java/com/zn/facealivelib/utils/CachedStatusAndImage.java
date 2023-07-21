package com.zn.facealivelib.utils;

import android.graphics.Bitmap;

import com.seeta.sdk.FaceAntiSpoofing;


public class CachedStatusAndImage {
    public static FaceAntiSpoofing.Status status = FaceAntiSpoofing.Status.DETECTING;
    public static boolean fasStatus = false;
    public static Bitmap detectedBitmap;
    public static int lastTime = 20;
    public static int currentFrameNum = 0;//消除摄像头曝光过程,当前帧索引

    //切换摄像头
    public static boolean frontCamera = true;//默认前向摄像头
}
