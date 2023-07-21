package com.zn.facealivelib.face;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zn.facealivelib.utils.FileUtils;

import java.io.File;

/**
 * author : 子宁
 * date : 2023/7/20 15:00
 * description :
 */
public class FaceHelper {
    public FaceHelper() {
    }
    private static class FaceHelperHolder{
        private static final FaceHelper Instance = new FaceHelper();
    }
    public static FaceHelper getInstance(){
        return FaceHelperHolder.Instance;
    }

    public static String MODELPATH;

    public void init(Context context)
    {
        //加入模型缓存
        String fdModel = "face_detector.csta";
        String pdModel = "face_landmarker_pts5.csta";
        String fasModel1 = "fas_first.csta";
        String fasModel2 = "fas_second.csta";

        File cacheDir = getExternalCacheDirectory(context, null);
        String modelPath = cacheDir.getAbsolutePath();
        MODELPATH = modelPath;

        if(!isExists(modelPath, fdModel))
        {
            File fdFile = new File(cacheDir + "/" + fdModel);
            FileUtils.copyFromAsset(context, fdModel, fdFile, false);
        }
        if(!isExists(modelPath, pdModel))
        {
            File pdFile = new File(cacheDir + "/" + pdModel);
            FileUtils.copyFromAsset(context, pdModel, pdFile, false);
        }
        if(!isExists(modelPath, fasModel1))
        {
            File fasFile1 = new File(cacheDir + "/" + fasModel1);
            FileUtils.copyFromAsset(context, fasModel1, fasFile1, false);
        }
        if(!isExists(modelPath, fasModel2))
        {
            File fasFile2 = new File(cacheDir + "/" + fasModel2);
            FileUtils.copyFromAsset(context, fasModel2, fasFile2, false);
        }
    }

    /**
     * 验证文件是否存在
     *
     * @param path
     * @param modelName
     * @return
     */
    public boolean isExists(String path, String modelName) {
        File file = new File(path + "/" + modelName);
        if (file.exists()) return true;
        return false;
    }

    public static File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)){
            appCacheDir = context.getExternalCacheDir();// /sdcard/data/data/app_package_name/cache
        }else {
            appCacheDir = new File(context.getFilesDir(),type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
            Log.e("getInternalDirectory","getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }
}
