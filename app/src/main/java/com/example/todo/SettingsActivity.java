package com.example.todo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import com.example.todo.databinding.ActivitySettingsBinding;

public class SettingsActivity extends ActivityDrawerBase {

    ActivitySettingsBinding activitySettingsBinding;


    private static final String SHARED_PREFERENCE_NAME = "settings";
    private static final String KEY_THEME = "dark_theme";
    private static final String KEY_LANGUAGE = "language";

    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_settings);
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitle(getString(R.string.settings));

        SettingFragment settingFragment = new SettingFragment();

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Boolean dark_theme = sharedPreferences.getBoolean("dark_theme",false);


        getSupportFragmentManager().beginTransaction().
                add(activitySettingsBinding.FrameLayoutSettings.getId(), settingFragment).commit();






    }


}
