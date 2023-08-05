package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login_button;
    private TextView registration_link, forgot_password;
    private FirebaseAuth firebaseAuth;
    private CheckBox rememberCheckbox;

    private final static String REMEMBER_CHECKBOX = "remember password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        login_button = findViewById(R.id.login_button);
        registration_link = findViewById(R.id.link_to_registration);
        forgot_password = findViewById(R.id.forgot_password);
        rememberCheckbox = findViewById(R.id.remember_checkbox);

        firebaseAuth = FirebaseAuth.getInstance();
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registration_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder dialogReset = new AlertDialog.Builder(view.getContext());
                //dialogReset.setTitle("Reset password");
                dialogReset.setMessage("Enter your email to reset password");
                dialogReset.setView(resetMail);

                dialogReset.setPositiveButton("Send link",(dialogInterface, i) ->
                        forgotPassword(resetMail.getText().toString()) );
                dialogReset.setNegativeButton("Cancel", (dialogInterface, i) -> {

                });
                dialogReset.show();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        rememberCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    editor.putBoolean(REMEMBER_CHECKBOX, true);
                    editor.apply();
                }else {
                    editor.putBoolean(REMEMBER_CHECKBOX, false);
                    editor.apply();
                }
            }
        });


    }

    private void forgotPassword(String email) {

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Email successfully sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Fail to send email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginUser() {

        String email_log, password_log;
        email_log = email.getText().toString();
        password_log = password.getText().toString();

        if (TextUtils.isEmpty(email_log)) {
            Toast.makeText(getApplicationContext(),"Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password_log)) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email_log,password_log).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}