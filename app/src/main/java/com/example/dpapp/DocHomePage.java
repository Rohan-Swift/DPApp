package com.example.dpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DocHomePage extends AppCompatActivity {


    ArrayList<String> docAssign = new ArrayList<>();
    TextView tv;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uname;
    ListView listView;
    Button signout;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_home_page);

        tv= findViewById(R.id.docName);

        mAuth= FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        listView= findViewById(R.id.listView);
        signout= findViewById(R.id.signOutBtn);

        user= mAuth.getCurrentUser();

        if(user!=null) {
            uname = user.getEmail();
            tv.setText(uname);
        }

        adapter=new MyAdapter(this, docAssign);
        listView.setAdapter(adapter);

        db.collection("users").whereEqualTo("Doc",uname).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Toast.makeText(DocHomePage.this, "Success", Toast.LENGTH_SHORT).show();
                for(QueryDocumentSnapshot doc: task.getResult()){
                    String data= doc.getString("Email");
                    docAssign.add(data);
                }
                adapter.notifyDataSetChanged();
            }
        });

        signout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent i = new Intent(DocHomePage.this, MainActivity.class);
            startActivity(i);
            finish();
        });

    }
}