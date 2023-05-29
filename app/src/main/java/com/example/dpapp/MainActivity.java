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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String text, UName, Passwd;

    EditText userName, Password;
    TextView tv;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button reg;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv= findViewById(R.id.toReg);
        userName= findViewById(R.id.LoginUName);
        Password= findViewById(R.id.LoginPass);
        radioGroup= findViewById(R.id.radgrp);
        reg=findViewById(R.id.loginBtn);

        mAuth= FirebaseAuth.getInstance();

        tv.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
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
                    Toast.makeText(MainActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                }
                if(Passwd.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Enter Password of atleast 6 characters", Toast.LENGTH_SHORT).show();
                    Password.requestFocus();
                }

                mAuth.signInWithEmailAndPassword(UName, Passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent i= new Intent(MainActivity.this, PatHomePage.class);
                            startActivity(i);
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
                    Toast.makeText(MainActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                }
                if(Passwd.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Enter Password of atleast 6 characters", Toast.LENGTH_SHORT).show();
                    Password.requestFocus();
                }

                mAuth.signInWithEmailAndPassword(UName, Passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent i = new Intent(MainActivity.this, DocHomePage.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });
    }
}