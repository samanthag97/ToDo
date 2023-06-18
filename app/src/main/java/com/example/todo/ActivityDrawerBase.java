package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class ActivityDrawerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(View view) {

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.container);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        //Fragment fragment = null;

        switch (item.getItemId()){
            //da fare gli altri casi, activity o fragment?
            case R.id.homepage:
                startActivity(new Intent(ActivityDrawerBase.this, MainActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.your_list:
                startActivity(new Intent(this, CustomFragmentActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.log_out:
                startActivity(new Intent(ActivityDrawerBase.this, LoginActivity.class));
                overridePendingTransition(0,0);
                finishAffinity(); //chiude app se provo a fare indietro
                break;
        }

        return false;
    }


    protected void  allocateActivityTitle(String title){

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }


}