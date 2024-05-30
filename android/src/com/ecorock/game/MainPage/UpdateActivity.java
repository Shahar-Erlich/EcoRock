package com.ecorock.game.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginModule;
import com.ecorock.game.Ui.Signup.SignUpModule;
import com.ecorock.game.Ui.Signup.SignupPage;
import com.ecorock.game.User;
import com.ecorock.game.currentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnClose,btnUpdate;
    ImageButton icon;
    EditText upname,upmail,uppass;
    private FirebaseFirestore db;
    String mailU,nameU,passU,mailS,nameS,passS;
    ImageButton i1,i2,i3,i4;
    private int prof=1;
    private  Dialog dialog;
    private interface Finish{
        void onReady(boolean nameExists,boolean emailExists);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Hide system bars and set behavior to show them transiently by swipe
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());


        // Get intent extras
        Intent intentG = getIntent();
        nameS = intentG.getStringExtra("name");
        mailS = intentG.getStringExtra("mail");
        passS = intentG.getStringExtra("pass");
        prof = intentG.getIntExtra("prof", 1);

        // Initialize UI elements
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClose = findViewById(R.id.btnCancel);
        upname = findViewById(R.id.editTextName);
        upmail = findViewById(R.id.editTextEmail);
        uppass = findViewById(R.id.editTextPassword);
        icon = findViewById(R.id.imageSelect);

        // Set initial text for EditText fields
        upname.setText(nameS);
        upmail.setText(mailS);
        uppass.setText(passS);

        // Set initial profile icon based on the prof value
        switch (prof){
            case 1:
                icon.setImageResource(R.drawable.defaulticondrawing);
                break;
            case 2:
                icon.setImageResource(R.drawable.icon_hand);
                break;
            case 3:
                icon.setImageResource(R.drawable.default_icon);
                break;
            case 4:
                icon.setImageResource(R.drawable.yugi_icon);
                break;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        // Set onClickListener for the update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailU = upmail.getText().toString();
                nameU = upname.getText().toString();
                passU = uppass.getText().toString();

                // Check if the email or name already exists
                checkMailExists(new Finish() {
                                        @Override
                                        public void onReady(boolean nameExists, boolean emailExists) {
                                            if (nameExists) {
                                                upname.setError("Username Already Exists!");
                                                return;
                                            }
                                            if (emailExists) {
                                                upmail.setError("Email Already Exists!");
                                                return;
                                            }

                                            // Update Firestore document with new user details
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
                                                                user.put("prof",prof);
                                                                // Update the document
                                                                db.collection("users")
                                                                        .document(id).update(user)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
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
                                            // Prepare intent with updated details and set the result
                                            Intent intent = new Intent();
                                            intent.putExtra("Nname", nameU);
                                            intent.putExtra("Nmail", mailU);
                                            intent.putExtra("Npass", passU);
                                            intent.putExtra("Nicon", prof);

                                            // Update current user details
                                            currentUser.setMail(mailU);
                                            currentUser.setName(nameU);
                                            currentUser.setPassword(passU);
                                            currentUser.setIcon(prof);

                                            // Set result and finish activity
                                            setResult(78, intent);
                                            UpdateActivity.super.onBackPressed();
                                        }
                                    },nameU,mailU);
    }
    });

        // Set onClickListener for the icon picker
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconPicker();
            }
        });


        // Set onClickListener for the close button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this,MainPage.class);
                startActivity(intent);
            }
        });
    }
        private void iconPicker() {
       dialog=new Dialog(this);
        dialog.setContentView(R.layout.update_dialog);
        Button btnDis;
        i1 = dialog.findViewById(R.id.i1);
        i2 = dialog.findViewById(R.id.i2);
        i3 = dialog.findViewById(R.id.i3);
        i4 = dialog.findViewById(R.id.i4);
        i1.setOnClickListener(this);
        i2.setOnClickListener(this);
        i3.setOnClickListener(this);
        i4.setOnClickListener(this);
        btnDis= dialog.findViewById(R.id.btnD);
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v==i1){
            prof =1;
            icon.setImageResource(R.drawable.defaulticondrawing);
            dialog.dismiss();
        }
        if(v==i2){
            prof =2;
            icon.setImageResource(R.drawable.icon_hand);
            dialog.dismiss();
        }
        if(v==i3){
            prof =3;
            icon.setImageResource(R.drawable.default_icon);
            dialog.dismiss();
        }
        if(v==i4){
            prof =4;
            icon.setImageResource(R.drawable.yugi_icon);
            dialog.dismiss();
        }
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
                        if (documentSnapshot.getData().get("email").equals(Mail) && !documentSnapshot.getData().get("email").equals(mailS)) {
                            emailExists = true;
                        }
                        // Check if the name exists and is different from the current user's name
                        if (documentSnapshot.getData().get("name").equals(Name) && !documentSnapshot.getData().get("name").equals(nameS)) {
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