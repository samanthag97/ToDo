package com.example.todo;

import android.os.Bundle;

import com.example.todo.databinding.FragmentSettingBinding;

public class SettingFragmentActivity extends ActivityDrawerBase{

    FragmentSettingBinding fragmentSettingBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSettingBinding = FragmentSettingBinding.inflate(getLayoutInflater());
        setContentView(fragmentSettingBinding.getRoot());
        allocateActivityTitle(getString(R.string.settings));

        SettingFragment settingFragment = new SettingFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //customFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_settings, settingFragment).commit();

    }
}
