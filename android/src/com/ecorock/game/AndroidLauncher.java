package com.ecorock.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ecorock.game.MainPage.MainPage;
import com.ecorock.game.Ui.MainPage.HomeScreen;

public class AndroidLauncher extends AndroidApplication implements GameScreen.MyGameCallback,SongPickingScreen.MyGameCallback{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		GameScreen gameScreen = new GameScreen(new EcoRockGame());
		gameScreen.setMyGameCallback(this);
		SongPickingScreen songPickingScreen = new SongPickingScreen(new EcoRockGame());
		Intent intent = getIntent();
		songPickingScreen.setMyGameCallback(this);
		songPickingScreen.setNumberOfLevels(intent.getIntExtra("level",-1));
		songPickingScreen.setFirstTime(intent.getBooleanExtra("firstTime",true));
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

	@Override
	public void changeFirst(boolean b) {
		MainPage.setFirstTime(b);
	}
}
