package com.example.todo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
        //here goes the code for doing something
        SwitchPreferenceCompat dark_theme_switch = findPreference("dark_theme");

        dark_theme_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object isDarkObject) {
                boolean isDark = (Boolean) isDarkObject;
                if(isDark){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getLayoutInflater().getContext().setTheme(R.style.Theme_Dark);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getLayoutInflater().getContext().setTheme(R.style.Theme_Light);
                }
                return true;
            }
        });



        /*if(dark_theme_switch.isChecked()){
            //dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getLayoutInflater().getContext().setTheme(R.style.Theme_Dark);
            return;
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getLayoutInflater().getContext().setTheme(R.style.Theme_Light);
            return;
        }*/

    }


}