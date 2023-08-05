package com.example.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SettingFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    Preference delete_data, delete_account;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);

        delete_data = findPreference("delete_data");
        delete_account = findPreference("delete_account");

        delete_data.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                alertDialog("Delete data");
                return true;
            }
        });

        delete_account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                alertDialog("Delete account");
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
                        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Account successfully deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Fail to delete account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Invalid password", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Data successfully deleted", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d(getTag(), "Error getting documents: ", task.getException());
                    }

                }
            });
        }
    }

    public void alertDialog(String title){
        View view = new View(getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(title);
        if(title.equals("Delete data")) {
            alertDialog.setMessage("This action will delete all your data." + "\n" +
                    "Do you want to proceed?");
        } else if (title.equals("Delete account")) {
            alertDialog.setMessage("This action will permanently delete your account and all your data." + "\n" +
                    "Do you want to proceed?");
        }
        alertDialog.setPositiveButton("Yes",(dialogInterface, i) ->{
            if(title.equals("Delete data")) {
                deleteData();
            } else if (title.equals("Delete account")) {
                passwordDialog();
            }
        });
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> {
        });
        alertDialog.show();
    }

    public void passwordDialog(){
        View view = new View(getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setMessage("Enter your password to confirm");
        EditText password = new EditText(view.getContext());
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(password);

        alertDialog.setPositiveButton("Delete account",(dialogInterface, i) -> {
            deleteAccount(password.getText().toString());
        });
        alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        alertDialog.show();
    }




}