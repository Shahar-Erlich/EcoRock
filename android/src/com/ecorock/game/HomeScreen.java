package com.ecorock.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button startGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    if(v == startGame){
        startActivity(new Intent(HomeScreen.this, AndroidLauncher.class));
    }
    }
}