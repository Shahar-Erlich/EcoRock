package com.ecorock.game.Ui.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginPage;
import com.ecorock.game.User;
import com.ecorock.game.Repository.repository;

public class SignupPage extends AppCompatActivity {
    private EditText name,email,pass;
    private Button btn;
    private repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        name = findViewById(R.id.edSignUpName);
        email = findViewById(R.id.edSignUpEmail);
        pass = findViewById(R.id.edSignUpPassword);
        btn = findViewById(R.id.BtnSignup);

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
                    repository.addUser(u);
                    startActivity(new Intent(SignupPage.this, LoginPage.class));
                }

            }
        });
    }
}