package com.ecorock.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ecorock.game.EcoRockGame;

public class AndroidLauncher extends AndroidApplication implements GameScreen.MyGameCallback{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		GameScreen gameScreen = new GameScreen(new EcoRockGame());
		gameScreen.setMyGameCallback(this);
		initialize(new EcoRockGame(), config);
	}

	@Override
	public void goToMenu() {
		startActivity(new Intent(AndroidLauncher.this,HomeScreen.class));
	}
}
