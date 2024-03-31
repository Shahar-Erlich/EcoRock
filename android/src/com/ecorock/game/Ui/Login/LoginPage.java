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
        forgot = findViewById(R.id.forgotPass);
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
        if(v== signup){
            startActivity(new Intent(LoginPage.this, SignupPage.class));
        }
        if(v == btnL){
            String Email = edMail.getText().toString();
            String Pass = edPass.getText().toString();
            User u = new User(Pass,Email);
            LoginModule loginModule = new LoginModule(u,this);
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get("email").toString().equals(u.getMail()) && document.getData().get("pass").toString().equals(u.getPass())) {
                                Intent intent = new Intent(LoginPage.this, MainPage.class);
                                u.setName(document.getData().get("name").toString());
                                u.setProf(Integer.parseInt(document.getData().get("prof").toString()));
                                u.setLevel(Integer.parseInt(document.getData().get("level").toString()));
                                if (rmmbr.isChecked()) {
                                    loginModule.SharedPreferences(u.getName(), Email, Pass, u.getProf(),u.getLevel());
                                } else {
                                    loginModule.removeDataSharedPreferences();
                                    intent.putExtra("username", u.getName());
                                    intent.putExtra("email", Email);
                                    intent.putExtra("password", Pass);
                                    intent.putExtra("prof", u.getProf());
                                    intent.putExtra("level",u.getLevel());

                                    //currentUser.setLevel(u.getLevel());
                                }
                                flag = true;
                                startActivity(intent);
                            }
                        }
                        if (flag == false) {
                            Toast.makeText(getBaseContext(), "Invalid mail or password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    }
            });

        }
    }

}