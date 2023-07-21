package com.zn.facealivever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.zn.facealivelib.face.FaceHelper;
import com.zn.facealivelib.mvp.PresenterImpl;
import com.zn.facealivever.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initPermission();
        FaceHelper.getInstance().init(this);
        mBinding.btnalive.setOnClickListener(v -> {
            FaceActivity.ResetStatus();
            startActivity(new Intent(this,FaceActivity.class));
        });
    }


    private void initPermission() {
        XXPermissions.with(this).permission(Permission.CAMERA).request(new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean allGranted) {

            }
        });
    }


}