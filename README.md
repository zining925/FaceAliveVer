# 基于seetaface6人脸识别（活体检测）的封装

# 使用

## Step 1. Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

## Step 2. Add the dependency

```
dependencies {
        implementation 'com.github.zining925:FaceAliveVer:1.0.1'
}
```

## step 3 Add it in your root build.gradle at the end of defaultConfig

```
ndk {
        abiFilters 'armeabi-v7a'
  }
```

## step 4 Add in Application

```
FaceHelper.getInstance().init(this);
```

# 下载体验

![img](https://www.pgyer.com/app/qrcode/w52uUA)
