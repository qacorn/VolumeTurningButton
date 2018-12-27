package com.qacorn.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2015/8/19.
 */
public class ScreenUtility {

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static float getScreenDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }
}
