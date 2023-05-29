package com.example.dpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatHomePage extends AppCompatActivity {

    TextView mail, signout, toHome;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pat_home_page);

        mail= findViewById(R.id.emailTV);
        signout= findViewById(R.id.patSignout);
        toHome= findViewById(R.id.tv5);

        mAuth= FirebaseAuth.getInstance();

         user=mAuth.getCurrentUser();


         toHome.setOnClickListener(view -> {
             Intent i= new Intent(PatHomePage.this, HomePage.class);
             startActivity(i);
         });

        if(user!=null) {
            email = user.getEmail();
            mail.setText(email);
        }

        signout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent i= new Intent(PatHomePage.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}