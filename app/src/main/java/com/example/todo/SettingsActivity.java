package com.example.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import com.example.todo.databinding.ActivitySettingsBinding;

import java.util.Locale;

public class SettingsActivity extends ActivityDrawerBase {

    ActivitySettingsBinding activitySettingsBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitle(getString(R.string.settings));

        SettingFragment settingFragment = new SettingFragment();

        getSupportFragmentManager().beginTransaction().
                add(activitySettingsBinding.FrameLayoutSettings.getId(), settingFragment).commit();

    }



}
