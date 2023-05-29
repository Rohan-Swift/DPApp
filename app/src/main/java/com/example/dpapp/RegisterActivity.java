package com.example.dpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    String text, UName, Passwd;

    EditText userName, Password;
    TextView tv;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button reg;
    
    int flag;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName= findViewById(R.id.RegUName);
        Password= findViewById(R.id.RegPass);
        radioGroup= findViewById(R.id.radgrp);
        reg= findViewById(R.id.regBtn);
        db= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        tv= findViewById(R.id.toLog);

        tv.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton= findViewById(i);
                text= radioButton.getText().toString();
            }
        });

        reg.setOnClickListener(view -> {
            if(text.equals("Patient")){
                UName= userName.getText().toString().trim();
                Passwd= Password.getText().toString().trim();
                if(UName.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                }
                if(Passwd.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Password of atleast 6 characters", Toast.LENGTH_SHORT).show();
                    Password.requestFocus();
                }

                mAuth.createUserWithEmailAndPassword(UName, Passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "User "+UName+" registered successfully", Toast.LENGTH_SHORT).show();
                            Map<String, Object> user= new HashMap<>();
                            user.put("Email", UName);
                            user.put("Role", text);

                            db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RegisterActivity.this, "User added to db", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            else if(text.equals("Doctor"))
            {
                UName= userName.getText().toString().trim();
                Passwd= Password.getText().toString().trim();
                if(UName.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                }
                if(Passwd.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Password of atleast 6 characters", Toast.LENGTH_SHORT).show();
                    Password.requestFocus();
                }

                mAuth.createUserWithEmailAndPassword(UName, Passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "User "+UName+" registered successfully", Toast.LENGTH_SHORT).show();
                            Map<String, Object> user= new HashMap<>();
                            user.put("Email", UName);
                            user.put("Role", text);

                            db.collection("doctors").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RegisterActivity.this, "User added to db", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });







//        reg.setOnClickListener(view -> {
//
//            UName= userName.getText().toString().trim();
//            Passwd= Password.getText().toString().trim();
//            if(UName.isEmpty())
//            {
//                Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
//                userName.requestFocus();
//            }
//            if(Passwd.isEmpty())
//            {
//                Toast.makeText(RegisterActivity.this, "Enter Password of atleast 6 characters", Toast.LENGTH_SHORT).show();
//                Password.requestFocus();
//            }
//
//            mAuth.createUserWithEmailAndPassword(UName, Passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful())
//                    {
//                        Toast.makeText(RegisterActivity.this, "User "+UName+" registered successfully", Toast.LENGTH_SHORT).show();
//                        Map<String, Object> user= new HashMap<>();
//                        user.put("Email", UName);
//                        user.put("Role", text);
//
//                        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(RegisterActivity.this, "User added to db", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            });
//
//        });
    }
}