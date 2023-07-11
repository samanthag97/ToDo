package com.example.todo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.todo.databinding.FragmentSettingsBinding;

public class SettingFragmentActivity extends ActivityDrawerBase {

    FragmentSettingsBinding fragmentSettingsBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allocateActivityTitle(getString(R.string.settings));

        setContentView(R.layout.activity_settings_fragment);
        //setContentView(fragmentSettingsBinding.getRoot());

        //non fa vedere il titolo e nemmeno il nav drawer...

        SettingFragment settingFragment = new SettingFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.FrameLayoutSettings, settingFragment)
                    .commit();
        }

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //customFragment.setArguments(getIntent().getExtras());

        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_settings, settingFragment).commit();

    }


}
