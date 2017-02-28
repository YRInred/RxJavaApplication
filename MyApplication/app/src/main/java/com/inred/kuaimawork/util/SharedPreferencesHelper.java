package com.inred.kuaimawork.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by inred on 2017/2/23.
 */
public class SharedPreferencesHelper {


    public static void putString(String filename, String key, String value) {
        SharedPreferences preferences = Utils.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putInt(String filename, String key, int value) {
        SharedPreferences preferences = Utils.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(String filename, String key) {
        String getValue;
        SharedPreferences preferences = Utils.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        getValue = preferences.getString(key, "");
        if (getValue.equals("null"))
            getValue = "";
        return getValue;
    }

    public static int getInt(String filename, String key) {
        int getValue = 0;
        SharedPreferences preferences = Utils.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        getValue = preferences.getInt(key, 0);
        return getValue;
    }

    public static void removeKeyValue(String filename, String key) {
        SharedPreferences preferences = Utils.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearSharedPreferences(String filename) {
        SharedPreferences preferences = Utils.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
