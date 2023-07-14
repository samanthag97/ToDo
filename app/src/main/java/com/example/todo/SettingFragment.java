package com.example.todo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

public class SettingFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    private static final String KEY_DARK_THEME = "dark_theme";
    private static final String KEY_LANGUAGE = "language";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //settings for dark theme
        SwitchPreferenceCompat dark_theme_switch = findPreference(KEY_DARK_THEME);
        dark_theme_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object isDarkObject) {

                boolean isDark = (Boolean) isDarkObject;

                if(isDark){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean(KEY_DARK_THEME,true).apply();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean(KEY_DARK_THEME,false).apply();
                }
                return true;
            }
        });

        //settings for language
        ListPreference language_select = findPreference(KEY_LANGUAGE);
        language_select.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object languageObject) {
                String language = (String) languageObject;

                if(language.equals("Italiano")){
                    setLocale("it");
                    editor.putString(KEY_LANGUAGE, "Italiano").apply();
                } else if (language.equals("Espanol")){
                    setLocale("es");
                    editor.putString(KEY_LANGUAGE, "Espanol").apply();
                }else{
                    setLocale("en");
                    editor.putString(KEY_LANGUAGE, "English").apply();
                }
                return true;
            }
        });

    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        getContext().getResources().updateConfiguration(configuration, getContext().
                getResources().getDisplayMetrics());

        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(0,0);
    }



}