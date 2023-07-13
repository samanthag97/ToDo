package com.example.todo;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class ApplicationManagement extends android.app.Application {

    private static ApplicationManagement applicationInstance;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        //applicationInstance = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDark = sharedPreferences.getBoolean("dark_theme", true);
        if(isDark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static ApplicationManagement get() {
        return applicationInstance;
    }

    /**
     * Gets shared preferences.
     *
     * @return the shared preferences
     */
    public static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationInstance);
        return sharedPreferences;
    }

    //set methods
    public static void setPreferences(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public static void setPreferences(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }

    public static void setPreferences(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public static void setPreferencesBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    //get methods
    public static String getPreferenceData(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static int getPreferenceDataInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static boolean getPreferenceDataBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static long getPreferenceDataLong(String interval) {
        return getSharedPreferences().getLong(interval, 0);
    }
}