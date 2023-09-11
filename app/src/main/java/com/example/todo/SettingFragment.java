package com.example.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class SettingFragment extends PreferenceFragmentCompat {

    private static final String KEY_LANGUAGE = "language";
    SharedPreferences sharedPreferences;
    Preference delete_data, delete_account;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ListPreference select_language;
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        delete_data = findPreference("delete_data");
        delete_account = findPreference("delete_account");
        select_language = findPreference(KEY_LANGUAGE);

        delete_data.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                alertDialog(getString(R.string.delete_data));
                return true;
            }
        });

        delete_account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                alertDialog(getString(R.string.delete_account));
                return true;
            }
        });

        select_language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object languageObject) {

                String language = (String) languageObject;

                if(language.equals("Italiano")){
                    setLocale("it");
                    editor.putString(KEY_LANGUAGE, "Italiano").apply();
                } else{
                    setLocale("en");
                    editor.putString(KEY_LANGUAGE, "English").apply();
                }

                return true;
            }
        });

    }

    public void deleteAccount(String password){
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPreferences.edit().clear().apply();

            String email = firebaseAuth.getCurrentUser().getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,password);

            firebaseAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()) {
                        deleteData();
                        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getContext(), R.string.account_not_deleted, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), R.string.invalid_psw, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    public void deleteData(){

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            String collectionPath = firebaseAuth.getCurrentUser().getUid();

            firestore = FirebaseFirestore.getInstance();
            firestore.collection(collectionPath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            String documentID = document.getId();
                            firestore.collection(collectionPath).document(documentID).delete();
                        }
                        Toast.makeText(getContext(), R.string.data_deleted, Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d(getTag(), "Error getting documents: ", task.getException());
                        Toast.makeText(getContext(), R.string.delete_data_fail, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void alertDialog(String title){
        View view = new View(getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(title);
        if(title.equals(getString(R.string.delete_data))) {
            alertDialog.setMessage(getString(R.string.alert_delete_data) + "\n" +
                    getString(R.string.proceed));
        } else if (title.equals(getString(R.string.delete_account))) {
            alertDialog.setMessage(getString(R.string.alert_delete_account) + "\n" +
                    getString(R.string.proceed));
        }
        alertDialog.setPositiveButton(R.string.yes,(dialogInterface, i) ->{

            if(title.equals(getString(R.string.delete_data))) {
                deleteData();
            } else if (title.equals(getString(R.string.delete_account))) {
                passwordDialog();
            }
        });
        alertDialog.setNegativeButton(R.string.no, (dialogInterface, i) -> {
        });
        alertDialog.show();
    }

    public void passwordDialog(){
        View view = new View(getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setMessage(R.string.psw_to_confirm_deleting);
        EditText password = new EditText(view.getContext());
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(password);

        alertDialog.setPositiveButton(R.string.delete_account,(dialogInterface, i) -> {
            deleteAccount(password.getText().toString());
        });
        alertDialog.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
        });
        alertDialog.show();
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