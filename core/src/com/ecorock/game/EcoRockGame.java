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
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class EcoRockGame extends ApplicationAdapter  implements InputProcessor {
	private Sound sound1,sound2,sound3,sound4;
	Stage stage;
	private Array<Rectangle> notes;
	private long lastNoteTime;
	private SpriteBatch batch;


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
		touch1.setSize(200,200);
		touch1.setBounds(0,150,200,200);
		touch2.setPosition(300,150);
		touch2.setSize(200,200);
		touch2.setBounds(300,150,200,200);
		touch3.setPosition(600,150);
		touch3.setSize(200,200);
		touch3.setBounds(600,150,200,200);
		touch4.setPosition(900,150);
		touch4.setSize(200,200);
		touch4.setBounds(900,150,200,200);


		Gdx.input.setInputProcessor(this);
		sound1 = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
		sound2 = Gdx.audio.newSound(Gdx.files.internal("guitar.mp3"));
		sound3 = Gdx.audio.newSound(Gdx.files.internal("ding.mp3"));
		sound4 = Gdx.audio.newSound(Gdx.files.internal("ping.mp3"));
		batch = new SpriteBatch();
		notes = new Array<Rectangle>();
		spawnNote();

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

		if(TimeUtils.nanoTime() - lastNoteTime > 1000000000) spawnNote();
		for (Iterator<Rectangle> iter = notes.iterator(); iter.hasNext(); ) {
			Rectangle note = iter.next();
			note.y -= 200 * Gdx.graphics.getDeltaTime();
			if(note.y + 64 < 0) iter.remove();
		}
		batch.begin();
		for(Rectangle raindrop: notes) {
			batch.draw(new Texture(Gdx.files.internal("dog.png")), raindrop.x, raindrop.y,64,128);
		}
		batch.end();

	}
	
	@Override
	public void dispose () {
	stage.dispose();
	sound1.dispose();
	sound2.dispose();
	sound3.dispose();
	sound4.dispose();
	}
	private void spawnNote() {
		Rectangle note = new Rectangle();
		note.x = MathUtils.random();
		note.y = Gdx.graphics.getHeight();
		note.width = 64;
		note.height = 128;
		notes.add(note);
		lastNoteTime = TimeUtils.nanoTime();
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
		Gdx.app.log("MyTag", coord.toString());
		if(hitButton!=null) {
			switch (hitButton.getName()) {
				case "t1":
					sound1.play();
					break;
			case "t2":
				sound2.play();
				break;
			case "t3":
				sound3.play();
				break;
			case "t4":
				sound4.play();
				break;
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
