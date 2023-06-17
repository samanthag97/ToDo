package com.example.todo;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.todo.databinding.FragmentCustomBinding;

public class CustomFragmentActivity extends ActivityDrawerBase{

    FragmentCustomBinding fragmentCustomBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCustomBinding = FragmentCustomBinding.inflate(getLayoutInflater());
        setContentView(fragmentCustomBinding.getRoot());
        allocateActivityTitle(getString(R.string.your_list));

        CustomFragment customFragment = new CustomFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //customFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_custom, customFragment).commit();

    }
}
