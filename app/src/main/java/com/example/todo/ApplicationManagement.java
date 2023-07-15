package com.example.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class ApplicationManagement extends android.app.Application {

    private static final String KEY_DARK_THEME = "dark_theme";
    private static final String KEY_LANGUAGE = "language";
    @Override
    public void onCreate() {


        //da aggiungere eccezione se è ==null
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isDark = sharedPreferences.getBoolean(KEY_DARK_THEME, true);
        String language = sharedPreferences.getString(KEY_LANGUAGE, "");

        if(isDark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate();

        if(language.equals("Italiano")){
            Locale locale = new Locale("it");
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().
                    getResources().getDisplayMetrics());

            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }

    }


}