package com.ecorock.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen, InputProcessor {
    final EcoRockGame game;

    private Sound sound1,sound2,sound3,sound4;
    Stage stage;
    private Array<Rectangle> notes;
    private long lastNoteTime;
    private Vector2 coord;
    private  boolean GAME_PAUSED = false;
    private Image touch1,touch2,touch3,touch4;
    private Texture noteT;
    Music music;
    long startTime;
    float[] secs;
    private FileHandle file;

    int noteId=0;
    public GameScreen(final EcoRockGame gam){
       this.game = gam;

        stage = new Stage(new ScreenViewport());
        Group group = new Group();
        touch1 = new Image(new Texture(Gdx.files.internal("dog.png")));
        touch2 = new Image(new Texture(Gdx.files.internal("dog.png")));
        touch3 = new Image(new Texture(Gdx.files.internal("dog.png")));
        touch4 = new Image(new Texture(Gdx.files.internal("dog.png")));
        Image pause = new Image(new Texture(Gdx.files.internal("dog.png")));

        touch1.setName("t1");
        touch2.setName("t2");
        touch3.setName("t3");
        touch4.setName("t4");
        pause.setName("pause");
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        root.bottom();
        root.add(touch1).size(200,200).space(60);
        root.add(touch2).size(200,200).space(60);
        root.add(touch3).size(200,200).space(60);
        root.add(touch4).size(200,200).space(60);
        noteT = new Texture(Gdx.files.internal("badlogic.jpg"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Undertale OST_ 090 - His Theme.mp3"));
        startTime = TimeUtils.millis();
        Gdx.input.setInputProcessor(this);
        sound1 = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("guitar.mp3"));
        sound3 = Gdx.audio.newSound(Gdx.files.internal("ding.mp3"));
        sound4 = Gdx.audio.newSound(Gdx.files.internal("ping.mp3"));
        game.batch = new SpriteBatch();
        notes = new Array<Rectangle>();
        spawnNote();
        file = Gdx.files.local("SongBeats/HisTheme.txt");
        beatProcessor bp = new beatProcessor();
        secs = bp.getSeconds();
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 0.2f, 1);

            stage.act();
            stage.draw();
            if (music.getPosition() >= secs[noteId]){
                spawnNote();
                noteId++;
            }
            for (Iterator<Rectangle> iter = notes.iterator(); iter.hasNext(); ) {
                Rectangle note = iter.next();
                note.y -= 600 * Gdx.graphics.getDeltaTime();
                if (note.y < -note.height) {
                    iter.remove();
                    Gdx.app.log("MyTag", "Removed");
                }

            }
            game.batch.begin();
            for (Rectangle raindrop : notes) {
                game.batch.draw(noteT, raindrop.x, raindrop.y, 64, 128);
            }
            game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        stage.dispose();
        sound1.dispose();
        sound2.dispose();
        sound3.dispose();
        sound4.dispose();
        game.batch.dispose();
        noteT.dispose();
    }
    private void spawnNote() {
        Rectangle note = new Rectangle();
        float[] pos = {50,350,650,950};
        note.x = getRandom(pos);
        note.y = Gdx.graphics.getHeight();
        note.width = 64;
        note.height = 128;
        notes.add(note);
    }
    public static float getRandom(float[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
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
        coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitButton = stage.hit(coord.x,coord.y,true);
       // Gdx.app.log("MyTag", coord.toString());
        Rectangle rectangle = new Rectangle();
        rectangle.setPosition(coord.x,coord.y);
        Rectangle hitRect = new Rectangle();
        if(hitButton!=null)hitRect.setPosition(hitButton.getX(),hitButton.getY());
        hitRect.setSize(200,200);
        rectangle.setSize(200,200);
        for (Iterator<Rectangle> iter = notes.iterator(); iter.hasNext(); ) {
            Rectangle note = iter.next();
            if (rectangle.overlaps(hitRect)&&hitRect.overlaps(note)) {
                iter.remove();
            }
        }
        String key="";
        if(hitButton!=null) {
            switch (hitButton.getName()) {
                case "t1":
                    key="1";
                    break;
                case "t2":
                    key="2";
                    break;
                case "t3":
                    key="3";
                    break;
                case "t4":
                    key="4";
                    break;
                case "pause":
                    GAME_PAUSED=true;
                    break;
            }

            if (key != "") {
            //file.writeString(key + "," + music.getPosition() +"\n",true);
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
