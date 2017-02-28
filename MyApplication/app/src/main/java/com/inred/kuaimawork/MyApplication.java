package com.inred.kuaimawork;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inred.kuaimawork.exception.CrashHandler;
import com.inred.kuaimawork.util.FileSystemHelper;
import com.inred.kuaimawork.util.ScreenUtils;
import com.inred.kuaimawork.util.StringConstants;
import com.inred.kuaimawork.util.Utils;
import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by inred on 2016/10/26.
 */
public class MyApplication extends Application {



    private static MyApplication appcation;

    private Toast toast;//主toast  20150721 inred

    public  static MyApplication getInstance(){
        return appcation;
    }

    private Stack<Activity> activityStack;

    @Override
    public void onCreate() {
        super.onCreate();
        appcation = this;
        Utils.init(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        initUniversalImageLoader();
    }

    private void initUniversalImageLoader() {
        try {
            FileSystemHelper.checkPathAndMakeThem(StringConstants.PHOTOCACHEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File sampleDir = new File(StringConstants.PHOTOCACHEPATH);

        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCache memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheExtraOptions(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight())
                .memoryCache(memoryCache)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiskCache(sampleDir))
                .diskCacheFileCount(100)
                .denyCacheImageMultipleSizesInMemory();
        if (BuildConfig.DEBUG) {
            config.writeDebugLogs();
        }
        ImageLoader.getInstance().init(config.build());
    }


    /*************/
    /****UI显示***/
    /**
     * *********
     */
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String text) {
        if (toast == null)
            toast = new Toast(this);
        LinearLayout linearLayout = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        textView.setText(text);
        linearLayout.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(linearLayout);
        toast.show();
    }


    /**
     * remove all(unless one) activity from list
     *
     * @param cls
     */
    public void removeAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            removeActivity(activity);
        }
    }

    /**
     * currentActivity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity=null;
        if (activityStack.size() > 0)
            activity = activityStack.lastElement();
        return activity;
    }

    /**
     * remove activity from list
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * remove last activity
     */
    public void removeActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * add activity in list
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null)
            activityStack = new Stack<Activity>();
        activityStack.add(activity);
    }




}
