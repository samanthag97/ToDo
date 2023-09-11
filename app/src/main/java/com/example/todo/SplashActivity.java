package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private final static String REMEMBER_CHECKBOX = "remember password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isChecked = sharedPreferences.getBoolean(REMEMBER_CHECKBOX, false);

        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally{
                    Intent intent;
                    if(isChecked){
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finishAffinity();
                }
            }
        };
        thread.start();
    }

}