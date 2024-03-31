package com.ecorock.game.Ui.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginPage;
import com.ecorock.game.User;
import com.ecorock.game.Repository.repository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupPage extends AppCompatActivity {
    private EditText name,email,pass;
    private Button btn;
    private repository repository;
    private FirebaseFirestore db;
    private interface Finish{
        void onReady(boolean nameExists,boolean emailExists);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(),getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
        name = findViewById(R.id.edSignUpName);
        email = findViewById(R.id.edSignUpEmail);
        pass = findViewById(R.id.edSignUpPassword);
        btn = findViewById(R.id.BtnSignup);
        db = FirebaseFirestore.getInstance();


        Context c = this;
        repository = new repository(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString();
                String Name = name.getText().toString();
                String Pass = pass.getText().toString();
                User u = new User(Name,Email,Pass);
                SignUpModule s = new SignUpModule(u,c);
                if(s.Check_User(name,email,pass))
                {
                    checkMailExists(new Finish() {
                        @Override
                        public void onReady(boolean nameExists, boolean emailExists) {
                            if(nameExists){
                                name.setError("Username Already Exists!");
                                return;
                            }
                            if(emailExists){
                                email.setError("Email Already Exists!");
                                return;
                            }
                            pass.setText("");
                            name.setText("");
                            email.setText("");
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", u.getName());
                            user.put("pass", u.getPass());
                            user.put("email", u.getMail());
                            user.put("level", 0);
                            user.put("prof", 1);
                            user.put("firstTime",true);
                            user.put("levelScores",new ArrayList<Integer>());
                            // Add a new document with a generated ID
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                            //repository.addUser(u);
                            startActivity(new Intent(SignupPage.this, LoginPage.class));
                        }
                    }, Name,Email);


                }

            }
        });
    }
    private void checkMailExists(Finish finish,String Name,String Mail){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean E=false,N=false;
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        if(documentSnapshot.getData().get("email").equals(Mail)){
                            E = true;
                        }
                        if(documentSnapshot.getData().get("name").equals(Name)){
                            N = true;
                        }
                    }
                    finish.onReady(N,E);
                }
            }
        });
    }
}