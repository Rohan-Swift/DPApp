package com.example.dpapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    String text, UName, Passwd;
    List<String> docList = new ArrayList<>();
    EditText userName, Password;
    TextView tv,regTV;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button reg;
    
    int flag;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    CollectionReference collectionRef;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName= findViewById(R.id.RegUName);
        Password= findViewById(R.id.RegPass);
        radioGroup= findViewById(R.id.radgrp);
        reg= findViewById(R.id.regBtn);
        regTV = findViewById(R.id.tv2);
        db= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        tv= findViewById(R.id.toLog);

        collectionRef = db.collection("doctors");
        query = collectionRef.orderBy("Email", Query.Direction.DESCENDING);

        tv.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String documentData = document.getString("Email"); // Replace "fieldName" with the actual field name in your document
                        docList.add(documentData);
                    }

                    // Perform any additional operations on the documentList as needed
                } else {
                    // Handle the error
                }
            }
        });

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(RegisterActivity.this, "Unable to retrieve data", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Process added documents
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot newDocument = dc.getDocument();
                        String email = newDocument.getString("Email");
                        docList.add(email);

                        // Perform any additional operations on the documentList as needed
                    }
                }
            }
        });



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton= findViewById(i);
                text= radioButton.getText().toString();
            }
        });





        regTV.setOnClickListener(view -> {
            for (String item : docList) {
                Toast.makeText(RegisterActivity.this, item, Toast.LENGTH_SHORT).show();
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

                            Collections.shuffle(docList);
                            Random random = new Random();
                            int randomIndex = random.nextInt(docList.size());
                            String randomValue = docList.get(randomIndex);
                            user.put("Doc", randomValue);

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