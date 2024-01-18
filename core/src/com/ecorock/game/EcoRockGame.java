package com.ecorock.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Iterator;

public class EcoRockGame extends ApplicationAdapter  implements InputProcessor {
	private Music bgm;
	private Sound sound1,sound2,sound3,sound4;
	private SpriteBatch batch;
	private Texture texture;
	private Array<Rectangle> dog;
	private OrthographicCamera camera;
	Stage stage;
	static int x=0;


	@Override
	public void create () {
//		bgm = Gdx.audio.newMusic(Gdx.files.internal("ViolinSonata.mp3"));
//		bgm.play();
		stage = new Stage(new ScreenViewport());
		Group group = new Group();
		Image touch1 = new Image(new Texture(Gdx.files.internal("dog.png")));
		Image touch2 = new Image(new Texture(Gdx.files.internal("badlogic.jpg")));
		Image touch3 = new Image(new Texture(Gdx.files.internal("dog.png")));
		Image touch4 = new Image(new Texture(Gdx.files.internal("badlogic.jpg")));

		touch1.setName("t1");
		touch2.setName("t2");
		touch3.setName("t3");
		touch4.setName("t4");

		group.addActor(touch1);
		group.addActor(touch2);
		group.addActor(touch3);
		group.addActor(touch4);
		stage.addActor(group);

		touch1.setPosition(0,150);
		touch1.setSize(300,300);
		touch1.setBounds(0,150,300,300);
		touch2.setPosition(300,150);
		touch2.setSize(300,300);
		touch2.setBounds(300,150,300,300);
		touch3.setPosition(600,150);
		touch3.setSize(300,300);
		touch3.setBounds(600,150,300,300);
		touch4.setPosition(900,150);
		touch4.setSize(300,300);
		touch4.setBounds(900,150,300,300);
		Gdx.input.setInputProcessor(this);
		sound1 = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
		sound2 = Gdx.audio.newSound(Gdx.files.internal("guitar.mp3"));
		sound3 = Gdx.audio.newSound(Gdx.files.internal("ding.mp3"));
		sound4 = Gdx.audio.newSound(Gdx.files.internal("ping.mp3"));
//		actor.addListener(new InputListener() {
//			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//				sound1.play();
//				return true;
//			}
//		});


	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenUtils.clear(0, 0, 0.2f, 1);
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose () {

	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
		Actor hitButton = stage.hit(coord.x,coord.y,true);
		if(hitButton!=null) {
			switch (hitButton.getName()) {
				case "t1":
					sound1.play();
					Gdx.app.log("MyTag", "1");
					return false;
			case "t2":
				sound2.play();
				Gdx.app.log("MyTag", "2");
				return false;
			case "t3":
				sound3.play();
				Gdx.app.log("MyTag", "3");
				return false;
			case "t4":
				sound4.play();
				Gdx.app.log("MyTag", "4");
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

}
