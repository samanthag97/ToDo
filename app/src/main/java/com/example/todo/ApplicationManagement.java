package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class ApplicationManagement extends android.app.Application {
    private static final String KEY_LANGUAGE = "language";


   @Override
    public void onCreate() {
       super.onCreate();
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       String language = sharedPreferences.getString(KEY_LANGUAGE, "");
       setLanguage(language);
    }

    public void setLanguage(String language){
       String lan="";
        if(language.equals("Italiano")) {
            lan = "it";
        }else{
            lan = "en";
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().
                getResources().getDisplayMetrics());
        //Intent intent = new Intent(this, SplashActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);
        }
    }