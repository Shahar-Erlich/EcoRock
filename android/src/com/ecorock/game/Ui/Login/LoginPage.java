package com.ecorock.game.Ui.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecorock.game.MainPage.AccountFragment;
import com.ecorock.game.MainPage.MainFragment;
import com.ecorock.game.MainPage.MainPage;
import com.ecorock.game.R;
import com.ecorock.game.Repository.repository;
import com.ecorock.game.Ui.Signup.SignupPage;
import com.ecorock.game.User;
import com.ecorock.game.currentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText edMail,edPass;
    private TextView forgot,signup;
    private Button btnL;
    private repository repository;
    private CheckBox rmmbr;
    private boolean flag=false;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(),getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
        edMail = findViewById(R.id.edName);
        edPass = findViewById(R.id.edPassword);
        signup = findViewById(R.id.signUp);
        btnL = findViewById(R.id.loginBtnR);
        rmmbr = findViewById(R.id.Rmmbrme);
        btnL.setOnClickListener(this);
        signup.setOnClickListener(this);
        repository = new repository(this);
        rmmbr.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        // Check if the signup button is clicked
        if (v == signup) {
            // Start the SignupPage activity
            startActivity(new Intent(LoginPage.this, SignupPage.class));
        }

        // Check if the login button is clicked
        if (v == btnL) {
            // Get the email and password from the input fields
            String Email = edMail.getText().toString();
            String Pass = edPass.getText().toString();

            // Create a new User object with the provided email and password
            User u = new User(Pass, Email);

            // Initialize a new LoginModule with the User object and the current context
            LoginModule loginModule = new LoginModule(u, this);

            // Access the Firestore database and get the collection of users
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    // Check if the task is successful
                    if (task.isSuccessful()) {
                        // Iterate through the document snapshots in the result
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Check if the email and password match
                            if (document.getData().get("email").toString().equals(u.getMail()) &&
                                    document.getData().get("pass").toString().equals(u.getPass())) {

                                // Create an intent to start the MainPage activity
                                Intent intent = new Intent(LoginPage.this, MainPage.class);

                                // Set the user's name, profile icon, and level from the document
                                u.setName(document.getData().get("name").toString());
                                u.setProf(Integer.parseInt(document.getData().get("prof").toString()));
                                u.setLevel(Integer.parseInt(document.getData().get("level").toString()));

                                // Check if the remember me checkbox is checked
                                if (rmmbr.isChecked()) {
                                    // Save the user details in shared preferences
                                    loginModule.SharedPreferences(u.getName(), Email, Pass, u.getProf(), u.getLevel());
                                } else {
                                    // Remove the saved user details from shared preferences
                                    loginModule.removeDataSharedPreferences();

                                    // Add user details to the intent
                                    intent.putExtra("username", u.getName());
                                    intent.putExtra("email", Email);
                                    intent.putExtra("password", Pass);
                                    intent.putExtra("prof", u.getProf());
                                    intent.putExtra("level", u.getLevel());
                                }

                                // Set the flag to true indicating a successful login
                                flag = true;

                                // Start the MainPage activity
                                startActivity(intent);
                            }
                        }
                        // If the flag is still false, show an error message
                        if (!flag) {
                            Toast.makeText(getBaseContext(), "Invalid mail or password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    }
            });

        }
    }

}