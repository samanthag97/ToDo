package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email, password_reg, password_repeat;
    private Button registration_button;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.email_registration);
        password_reg = findViewById(R.id.password_registration);
        password_repeat = findViewById(R.id.repeat_password);
        registration_button = findViewById(R.id.registration_button);
        firebaseAuth = FirebaseAuth.getInstance();



        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().clear().apply();
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {

        String email_reg, password_register, password_rep;
        email_reg = email.getText().toString();
        password_register = password_reg.getText().toString();
        password_rep = password_repeat.getText().toString();

        if (TextUtils.isEmpty(email_reg)) {
            Toast.makeText(getApplicationContext(),R.string.pls_enter_mail, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password_register)) {
            Toast.makeText(getApplicationContext(), R.string.pls_enter_psw, Toast.LENGTH_LONG).show();
            return;
        }
        if(!password_rep.equals(password_register)){
            Toast.makeText(getApplicationContext(), R.string.check_same_psw, Toast.LENGTH_LONG).show();
            return;
        }
        if(password_register.length()<6){
            Toast.makeText(getApplicationContext(), R.string.psw_6_char, Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email_reg,password_register).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), R.string.reg_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.reg_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}