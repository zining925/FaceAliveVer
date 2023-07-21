package com.zn.facealivelib.mvp;

import android.graphics.Bitmap;
import android.view.TextureView;


import com.seeta.sdk.FaceAntiSpoofing;

import org.opencv.core.Mat;
import org.opencv.core.Rect;


public interface VerificationContract {

    interface View {

        void drawFaceRect(Rect faceRect);

        void toastMessage(String msg);

        void showCameraUnavailableDialog(int errorCode);

        void setStatus(FaceAntiSpoofing.Status status, Mat matBgr, Rect faceRect, Bitmap b);

        void setPresenter(Presenter presenter);

        TextureView getTextureView();

    }

    interface Presenter {

        void detect(byte[] data, int width, int height, int rotation);

        void destroy();

    }
}
