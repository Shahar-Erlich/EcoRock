package com.ecorock.game.Ui.MainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ecorock.game.AndroidLauncher;
import com.ecorock.game.MainPage.MainPage;
import com.ecorock.game.R;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button startGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(this);
    }

    public void onClick(View v) {
    if(v == startGame){
        startActivity(new Intent(HomeScreen.this, MainPage.class));
    }
    }
}