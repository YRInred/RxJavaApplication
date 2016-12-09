package com.inred.kuaimawork.util;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.inred.kuaimawork.MyApplication;

/**
 * Created by Administrator on 2016/6/23.
 */
public class ScreenUtils {
    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int dip2px(float dpValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MyApplication.getInstance().getResources().getDisplayMetrics());
    }
    public static int px2dip(float pxValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
