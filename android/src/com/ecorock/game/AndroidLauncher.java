package com.ecorock.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ecorock.game.MainPage.MainPage;
import com.ecorock.game.Ui.MainPage.HomeScreen;

public class AndroidLauncher extends AndroidApplication implements GameScreen.MyGameCallback,SongPickingScreen.MyGameCallback{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create configuration for the Android application
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false; // Disable accelerometer
		config.useCompass = false; // Disable compass

		// Create instances of GameScreen and SongPickingScreen
		GameScreen gameScreen = new GameScreen(new EcoRockGame());
		gameScreen.setMyGameCallback(this); // Set callback for GameScreen
		SongPickingScreen songPickingScreen = new SongPickingScreen(new EcoRockGame());
		songPickingScreen.setMyGameCallback(this); // Set callback for SongPickingScreen

		// Retrieve intent to get the number of levels
		Intent intent = getIntent();
		songPickingScreen.setNumberOfLevels(intent.getIntExtra("level", -1)); // Set the number of levels in SongPickingScreen

		// Initialize the EcoRockGame with the given configuration
		initialize(new EcoRockGame(), config);
	}

	@Override
	public void goToMenu() {
		startActivity(new Intent(AndroidLauncher.this, HomeScreen.class));
	}

	@Override
	public void levelEnd(int level,int score,int levelI) {
		Intent intent = new Intent(AndroidLauncher.this, MainPage.class);
		intent.putExtra("level",level);
		intent.putExtra("score",score);
		intent.putExtra("levelI",levelI);
		startActivity(intent);
	}

	public void goBack() {
		startActivity(new Intent(AndroidLauncher.this, MainPage.class));
	}

}
