package com.ecorock.game.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Repository.repository;
import com.ecorock.game.Ui.Login.LoginModule;
import com.ecorock.game.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    Button btnClose,btnUpdate;
    ImageButton icon;
    EditText upname,upmail,uppass;
    private FirebaseFirestore db;
    String mailU,nameU,passU,mailS,nameS,passS;
    private com.ecorock.game.Repository.repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intentG = getIntent();
        nameS = intentG.getStringExtra("name");
        mailS = intentG.getStringExtra("mail");
        passS = intentG.getStringExtra("pass");
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClose= findViewById(R.id.btnCancel);
        upname = findViewById(R.id.editTextName);
        upmail = findViewById(R.id.editTextEmail);
        uppass = findViewById(R.id.editTextPassword);
        upname.setText(nameS);
        upmail.setText(mailS);
        uppass.setText(passS);
        icon = findViewById(R.id.imageSelect);
        db=FirebaseFirestore.getInstance();
        repository = new repository(UpdateActivity.this);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailU = upmail.getText().toString();
                nameU = upname.getText().toString();
                passU = uppass.getText().toString();
                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("email").toString().equals(mailS)) {
                                    String id = document.getId();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", nameU);
                                    user.put("pass", passU);
                                    user.put("email", mailU);
                                    // Add a new document with a generated ID
                                    db.collection("users")
                                            .document(id).set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(UpdateActivity.this, "lmao", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                }
                            }
                        }
                    }
                });

                LoginModule loginModule = new LoginModule(new User("","",""),UpdateActivity.this);
                loginModule.SharedPreferences(nameU,mailU,passU);
                repository.updateUser(mailU,new User(nameU,mailU,passU));
                Intent intent = new Intent();
                intent.putExtra("Nname",nameU);
                intent.putExtra("Nmail",mailU);
                intent.putExtra("Npass",passU);
                setResult(78,intent);
                UpdateActivity.super.onBackPressed();
    }
    });
    }}