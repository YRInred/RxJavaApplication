package com.inred.kuaimawork.util;

import android.os.Environment;

/**
 * Created by inred on 2015/4/15.
 */
public class StringConstants {





    //nomalPath
    public static final String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String KUAIMAPATH = SDPATH + "/Kuaima";//根路径
    public static final String FILEPATH = KUAIMAPATH+"/file";//文件路径
    public static final String APKDOWNPATH = KUAIMAPATH+"/apk";//APK下载路径
    public static final String PHOTOPATH = KUAIMAPATH+"/photo";//图片路径
    public static final String PHOTOCACHEPATH = PHOTOPATH+"/cache";//图片缓存路径
    public static final String VIDEOPATH = KUAIMAPATH+"/video";//视频路径
    public static final String VOICEPATH = KUAIMAPATH+"/voice";//音频路径
    public static final String CRASHPATH = KUAIMAPATH+"/crash";//log 路径


}
