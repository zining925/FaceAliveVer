package com.zn.facealivever;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.seeta.sdk.FaceAntiSpoofing;
import com.zn.facealivelib.constants.camera.CameraCallbacks;
import com.zn.facealivelib.face.FaceHelper;
import com.zn.facealivelib.mvp.PresenterImpl;
import com.zn.facealivelib.mvp.VerificationContract;
import com.zn.facealivelib.utils.CachedStatusAndImage;
import com.zn.facealivever.databinding.ActivityFaceBinding;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class FaceActivity extends AppCompatActivity implements VerificationContract.View{
    private ActivityFaceBinding mBinding;

    private VerificationContract.Presenter mPresenter;
    private AlertDialog mCameraUnavailableDialog;
    private Camera.Size mPreviewSize;

    private SurfaceHolder mOverlapHolder;
    private android.graphics.Rect focusRect = new android.graphics.Rect();
    private Paint mFaceRectPaint = null;
    private Paint mFaceNamePaint = null;

    private float mPreviewScaleX = 1.0f;
    private float mPreviewScaleY = 1.0f;

    private int mCurrentStatus = 0;
    private Mat mImageAfterBlink = null;
    private org.opencv.core.Rect mFaceRectAfterBlink = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFaceBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        new PresenterImpl(this);
        mFaceRectPaint = new Paint();
        mFaceRectPaint.setColor(Color.argb(150, 0, 255, 0));
        mFaceRectPaint.setStrokeWidth(3);
        mFaceRectPaint.setStyle(Paint.Style.STROKE);

        mFaceNamePaint = new Paint();
        mFaceNamePaint.setColor(Color.argb(150, 0,255, 0));
        mFaceNamePaint.setTextSize(50);
        mFaceNamePaint.setStyle(Paint.Style.FILL);

        mBinding.surfaceViewOverlap.setZOrderOnTop(true);
        mBinding.surfaceViewOverlap.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mOverlapHolder = mBinding.surfaceViewOverlap.getHolder();

        mBinding.cameraPreview.setCameraCallbacks(mCameraCallbacks);

    }

    private CameraCallbacks mCameraCallbacks = new CameraCallbacks() {
        @Override
        public void onCameraUnavailable(int errorCode) {
            showCameraUnavailableDialog(errorCode);
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mPreviewSize == null) {
                mPreviewSize = camera.getParameters().getPreviewSize();

                mPreviewScaleX = (float) (mBinding.cameraPreview.getHeight()) / mPreviewSize.width;
                mPreviewScaleY = (float) (mBinding.cameraPreview.getWidth()) / mPreviewSize.height;
            }

            mPresenter.detect(data, mPreviewSize.width, mPreviewSize.height,
                    mBinding.cameraPreview.getCameraRotation());
        }
    };


    @Override
    public void drawFaceRect(org.opencv.core.Rect faceRect) {
        Canvas canvas = mOverlapHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (faceRect != null) {
            faceRect.x *= mPreviewScaleX;
            faceRect.y *= mPreviewScaleY;
            faceRect.width *= mPreviewScaleX;
            faceRect.height *= mPreviewScaleY;

            focusRect.left = faceRect.x;
            focusRect.right = faceRect.x + faceRect.width;
            focusRect.top = faceRect.y;
            focusRect.bottom = faceRect.y + faceRect.height;

            canvas.drawRect(focusRect, mFaceRectPaint);
        }

        mOverlapHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void toastMessage(String msg) {
        LogUtils.d(msg);
    }

    @Override
    public void showCameraUnavailableDialog(int errorCode) {
        if(mCameraUnavailableDialog == null) {
            mCameraUnavailableDialog = new AlertDialog.Builder(this)
                    .setTitle("摄像头不可用")
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recreate();
                                }
                            });
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }
                    })
                    .create();
        }
        if(!mCameraUnavailableDialog.isShowing()) {
            mCameraUnavailableDialog.show();
        }
    }

    @Override
    public void setStatus(FaceAntiSpoofing.Status status, Mat matBgr, Rect faceRect, Bitmap b) {
        String tip = "";
        if(null == status) {//正在检测中的标志
            mBinding.txtStatus.setText("正在检测中...");
        } else {
            switch (status) {
                case DETECTING:
                    tip = "没有人脸";
                    mBinding.txtStatus.setText(tip);
                    break;
                case REAL:
                    tip = "真人脸";
                    LogUtils.eTag("hello",tip);
                    LogUtils.eTag("hello",faceRect);
                    mBinding.iv.setBackground(ImageUtils.bitmap2Drawable(ImageUtils.rotate(b,180,0,100)));
                    mBinding.txtStatus.setText(tip);
                    break;
                case SPOOF:
                    tip = "假人脸";
                    LogUtils.dTag("hello",tip);
                    LogUtils.dTag("hello",faceRect);
                    mBinding.txtStatus.setText(tip);
                    break;
                case FUZZY:
                    tip = "图像过于模糊";
                    LogUtils.dTag("hello",tip);
                    LogUtils.dTag("hello",faceRect);
                    mBinding.txtStatus.setText(tip);
                    break;
            }
        }
    }


    @Override
    public void setPresenter(VerificationContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public TextureView getTextureView() {
        return mBinding.cameraPreview;

    }

    public static void ResetStatus()
    {
        CachedStatusAndImage.status = FaceAntiSpoofing.Status.DETECTING;
        CachedStatusAndImage.currentFrameNum = 0;
    }


}