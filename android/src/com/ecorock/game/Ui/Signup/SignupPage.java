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

import com.ecorock.game.MainPage.UpdateActivity;
import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginPage;
import com.ecorock.game.User;
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
    private FirebaseFirestore db;
    private interface Finish{
        void onReady(boolean nameExists,boolean emailExists);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        // Hide the system bars for an immersive experience
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        // Initialize UI components
        name = findViewById(R.id.edSignUpName);
        email = findViewById(R.id.edSignUpEmail);
        pass = findViewById(R.id.edSignUpPassword);
        btn = findViewById(R.id.BtnSignup);
        db = FirebaseFirestore.getInstance();


        // Get context
        Context c = this;

        // Set onClickListener for the sign-up button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the entered details
                String Email = email.getText().toString();
                String Name = name.getText().toString();
                String Pass = pass.getText().toString();

                // Create a new User object
                User u = new User(Name, Email, Pass);
                // Initialize the SignUpModule with the User object and context
                SignUpModule s = new SignUpModule(u,c);

                // Check if the user input is valid
                if (s.Check_User(name, email, pass)) {
                    // Check if the email or username already exists in the database
                    checkMailExists(new Finish() {
                        @Override
                        public void onReady(boolean nameExists, boolean emailExists) {
                            // Handle existing username
                            if (nameExists) {
                                name.setError("Username Already Exists!");
                                return;
                            }
                            // Handle existing email
                            if (emailExists) {
                                email.setError("Email Already Exists!");
                                return;
                            }

                            // Clear the input fields
                            pass.setText("");
                            name.setText("");
                            email.setText("");

                            // Create a map to store user details
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("name", u.getName());
                            userMap.put("pass", u.getPass());
                            userMap.put("email", u.getMail());
                            userMap.put("level", 0);
                            userMap.put("prof", 1);
                            userMap.put("levelScores", new ArrayList<Integer>());

                            // Add the user to the Firestore database
                            db.collection("users")
                                    .add(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Handle success
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure
                                        }
                                    });

                            // Start the LoginPage activity
                            startActivity(new Intent(SignupPage.this, LoginPage.class));
                        }
                    }, Name, Email);


                }

            }
        });
    }
    private void checkMailExists(Finish finish, String Name, String Mail){
        // Access the Firestore database and get the collection of users
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean emailExists = false, nameExists = false;
                // Check if the task is successful
                if (task.isSuccessful()) {
                    // Iterate through the document snapshots in the result
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        // Check if the email exists and is different from the current user's email
                        if (documentSnapshot.getData().get("email").equals(Mail) && !documentSnapshot.getData().get("email").equals(Mail)) {
                            emailExists = true;
                        }
                        // Check if the name exists and is different from the current user's name
                        if (documentSnapshot.getData().get("name").equals(Name) && !documentSnapshot.getData().get("name").equals(Name)) {
                            nameExists = true;
                        }
                    }
                    // Call the onReady method of the Finish interface with the results
                    finish.onReady(nameExists, emailExists);
                }
            }
        });
    }
}