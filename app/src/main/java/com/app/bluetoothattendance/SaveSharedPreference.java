package com.app.bluetoothattendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.SharedPreferences.Editor;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_NIM= "NIM";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setNIM(Context ctx, String NIM)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NIM, NIM);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getNIM(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NIM, "");
    }

    public static void clearUserName(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
