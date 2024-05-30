package com.ecorock.game;
// Importing necessary libraries from libGDX and Java
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


public class GameScreen implements Screen, InputProcessor {// Main game screen class implementing libGDX's Screen and InputProcessor interfaces

    public interface MyGameCallback { // Interface for callback methods to interact with the game
        public void goToMenu();
        public void levelEnd(int level,int score,int i);
    }

    final EcoRockGame game;// Reference to the main game class
    private static MyGameCallback myGameCallback;// Local variable to hold the callback implementation
    private Sound sound1,sound2,sound3,sound4;
    Stage stage,pStage,eStage; // Stages for different UI layers
    private Array<Rectangle> notes;// Array to hold note rectangles
    private Vector2 coord;// Coordinate vector for positioning
    private  boolean RUNNING,isDeleted=true,longPressed=false,ended=false;// Game state flags
    private Button touch1,touch2,touch3,touch4, pause;// Buttons for user input
    private Texture noteT,noteT2,BGT,ButtonT,appleT,canT,bagT,bananaT;// Textures for graphics
    private Music music;// Music for background score


    // Timing and score variables
    private long startTime;
    private Label done;
    private float[] secs;
    private Array<Float> longs;
    private int[] pos;
    private String key="", temp="",TimingScore="";
    private FileHandle file;
    private int Score=0,removeNoteInd,longIndI=0;
    private TextureAtlas atlas;
    private Skin skin;
    private TextButton resume,songP,mainMenu,end;
    private static int notesMissed;
    private Label ScoreL;
    private float touchStart,touchEnd,noteY ,noteH,timeSeconds = 0f,downT,upT;
    private  ProgressBar progressBar;
    private  InputMultiplexer multiplexer;
    private Array<Integer> longInd;
    private Array<Texture> noteTs;
    private static String texturePath="skin_default.png";
    private Texture tempT;
    private float fadeTime=1;
    private int levelI,uLevel,diffN;
    public boolean endTime=false;

    public void setMyGameCallback(MyGameCallback callback) {
        myGameCallback = callback;
    }// Method to set the callback implementation
    public GameScreen(final EcoRockGame game){this.game = game;}// Constructor to initialize game screen

    public GameScreen(final EcoRockGame gam,FileHandle chosenSongBeat,FileHandle chosenSong,int levelNum,int userLevel,int diff){// Overloaded constructor with additional parameters for game setup
        notesMissed=0;
        diffN = diff;
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this);
       this.game = gam;
       this.file = chosenSongBeat;
        music = Gdx.audio.newMusic(chosenSong);
        music.play();
        music.setLooping(false);
        stage = new Stage(new ScreenViewport());
        pStage = new Stage(new ScreenViewport());
        eStage = new Stage(new ScreenViewport());
        noteT = new Texture(Gdx.files.internal("NoteTest.png"));
        noteT2 = new Texture(Gdx.files.internal("can.png"));
        appleT =new Texture(Gdx.files.internal("apple.png"));
        bananaT =new Texture(Gdx.files.internal("banana.png"));
        canT =new Texture(Gdx.files.internal("can.png"));
        bagT =new Texture(Gdx.files.internal("tBag.png"));
        BGT = new Texture(Gdx.files.internal(texturePath));
        ButtonT = new Texture(Gdx.files.internal("ButtonTest.png"));
        atlas = new TextureAtlas("ui/arcade-ui.atlas");
        skin = new Skin(Gdx.files.internal("ui/arcade-ui.json"),atlas);
        touch1 = new Button(skin);
        touch2 = new Button(skin);
        touch3 = new Button(skin);
        touch4 = new Button(skin);
        pause = new Button(skin);
        createPause(pStage);
        createEnd(eStage);
        touch1.setName("t1");
        touch2.setName("t2");
        touch3.setName("t3");
        touch4.setName("t4");
        pause.setName("pause");
        Table root = new Table();
        root.setFillParent(true);
        root.bottom();
        Table rootUi = new Table();
        rootUi.setFillParent(true);
        rootUi.top();
        progressBar = new ProgressBar(0, 100, .1f, false,new Skin(Gdx.files.internal("ui/progress-ui.json"),new TextureAtlas("ui/progress-ui.atlas")));
        progressBar.setName("PB");
        rootUi.add(progressBar).growX();
        rootUi.row();
        int space =45;
        rootUi.add(pause).size(200,200).growX().align(Align.right);
        root.add(touch1).size(200,200).pad(space);
        root.add(touch2).size(200,200).pad(space);
        root.add(touch3).size(200,200).pad(space);
        root.add(touch4).size(200,200).pad(space);
        stage.addActor(root);
        stage.addActor(rootUi);
        multiplexer.addProcessor(stage);
        startTime = TimeUtils.millis();
        sound1 = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("guitar.mp3"));
        sound3 = Gdx.audio.newSound(Gdx.files.internal("ding.mp3"));
        sound4 = Gdx.audio.newSound(Gdx.files.internal("ping.mp3"));
        game.batch = new SpriteBatch();
        notes = new Array<Rectangle>();
        longInd = new Array<Integer>();
        noteTs = new Array<Texture>();
        beatProcessor bp = new beatProcessor(file);
        secs = bp.getSeconds();
        pos = bp.getPos();
        longs = bp.getLongs();
        createNotes();
        RUNNING = true;
        uLevel = userLevel;
        levelI = levelNum;
    }

    public static void changeBGT(String path){
        texturePath = path;
    }// Static method to change background texture
    @Override
    public void show() {

    }
    public void createNotes(){// Method to create and add notes to the game screen
        float posI=0;
        double height=128;

        // Act and draw the stage to ensure it's updated
        stage.act();
        stage.draw();

        // Iterate through each second in the secs array
        for (int i = 0; i < secs.length; i++) {
            height=128;
            // Determine the position of the note based on the value in pos array
            switch ((pos[i])){
                case 1:
                    // Convert local touch coordinates to stage coordinates for touch1
                    Vector2 t1C = touch1.localToStageCoordinates(new Vector2(0,0));
                    posI = t1C.x;
                    break;
                case 2:
                    // Convert local touch coordinates to stage coordinates for touch2
                    Vector2 t2C = touch2.localToStageCoordinates(new Vector2(0,0));
                    posI = t2C.x;
                    break;
                case 3:
                    // Convert local touch coordinates to stage coordinates for touch3
                    Vector2 t3C = touch3.localToStageCoordinates(new Vector2(0,0));
                    posI = t3C.x;
                    break;
                case 4:
                    // Convert local touch coordinates to stage coordinates for touch4
                    Vector2 t4C = touch4.localToStageCoordinates(new Vector2(0,0));
                    posI = t4C.x;
                    break;

            }
            // Determine the height of the note based on the value in longs array
            if(longs.get(i)!=0 && longs.get(i)*600>150){
                height = Math.floor(longs.get(i)*600);
                longInd.add(1);// Mark as a long note
            }
            else{
                longInd.add(0);// Mark as a short note
            }
            // Spawn the note with calculated position and height
        spawnNote(secs[i]*600,posI,(float)height);
        }
    }
    @Override
    public void render(float delta) {
        // Clear the screen with the specified color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Begin drawing with the stage's batch
        stage.getBatch().begin();
        stage.getBatch().draw(BGT,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.getBatch().end();

        // Check if the music volume is 0 or less
        if(music.getVolume()<=0){
            if(ended==false){
                // Add the event stage processor to the input multiplexer if the song has ended
                multiplexer.addProcessor(eStage);
            }

            // Draw the "noteT" texture on the screen
            stage.getBatch().begin();
            stage.getBatch().draw(noteT,0,resume.getY()-400,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2);
            stage.getBatch().end();

            // Update the status flags and stop the music
            ended=true;
            RUNNING=false;
            music.stop();

            // Act and draw the end stage
            eStage.act();
            eStage.draw();
        }

        // Update the timer if the game is running
        if(RUNNING){
            timeSeconds +=Gdx.graphics.getDeltaTime();
        }

        // Fade out the music if more than 60 seconds have passed and the music is still playing
        if(timeSeconds>60&&music.isPlaying()){
            fadeTime-= delta * 0.7;
            music.setVolume(fadeTime);
        }

        // Stop the game and switch screens if the number of missed notes equals the difficulty number
         if(notesMissed==diffN){
           music.stop();
          game.setScreen(new SongPickingScreen(game,""));
          return;
       }

        // Game logic for updating and drawing notes
            if(RUNNING) {
                // Update the progress bar value
                progressBar.setValue((timeSeconds/60)*100);
                // Act and draw the stage
                stage.act();
                stage.draw();


                if(isDeleted==false){
                    // Update the height and position of the note being removed
                    noteH=notes.get(removeNoteInd).getHeight();
                    noteY=notes.get(removeNoteInd).getY();
                    notes.get(removeNoteInd).setY(noteY+600*Gdx.graphics.getDeltaTime());
                    notes.get(removeNoteInd).setHeight(noteH-600*Gdx.graphics.getDeltaTime());
                    Score+=1;

                    // Remove the note if its height is less than or equal to 1
                    if(notes.get(removeNoteInd).getHeight()<=1){
                        isDeleted=true;
                        notes.removeIndex(removeNoteInd);
                        longs.removeIndex(removeNoteInd);
                        longInd.removeIndex(removeNoteInd);
                        noteTs.removeIndex(removeNoteInd);
                    }
                }

                // Move the notes and handle notes that go off-screen
                for (Rectangle note: notes) {
                    note.y -= 600 * Gdx.graphics.getDeltaTime();
                    if (note.y < -note.height) {
                        if(isDeleted==false){
                            isDeleted=true;
                        }
                        longs.removeIndex(notes.indexOf(note,false));
                        longInd.removeIndex(notes.indexOf(note,false));
                        noteTs.removeIndex(notes.indexOf(note,false));
                        notes.removeValue(note,true);

                            notesMissed++;
                    }
                }

                // Begin drawing with the game batch
                game.batch.begin();
                // Draw the score on the screen
                game.font.draw(game.batch, String.valueOf(Score),(Gdx.graphics.getWidth()/4),Gdx.graphics.getHeight());

                // Draw the notes
                for (Rectangle note : notes) {
                    if(longInd.get(notes.indexOf(note,true))==1){
                        game.batch.draw(noteT, note.x, note.y,200, note.height);
                        game.batch.draw(noteTs.get(notes.indexOf(note,false)), note.x, note.y, 200, 200);
                    }
                    else{
                        game.batch.draw(noteTs.get(notes.indexOf(note,false)), note.x, note.y, 200, 200);
                    }
                }
                // End drawing with the game batch
                game.batch.end();
            }
            else if(ended==false){
                // Begin drawing with the game batch
                game.batch.begin();

                // Draw the notes
                for (Rectangle note : notes) {
                    if(longInd.get(notes.indexOf(note,true))==1) {
                        game.batch.draw(noteT, note.x, note.y, 200, note.height);
                        game.batch.draw(noteTs.get(notes.indexOf(note, false)), note.x, note.y, 200, 200);
                    }
                    else{
                        game.batch.draw(noteTs.get(notes.indexOf(note,false)), note.x, note.y, 200, 200);
                    }
                }
                // End drawing with the game batch
                game.batch.end();
                // Pause the music
                music.pause();

                // Draw the main stage and the paused stage
                stage.draw();
                pStage.act();
                pStage.draw();
            }
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
        BGT.dispose();
        ButtonT.dispose();
        tempT.dispose();
        appleT.dispose();
        bananaT.dispose();
        canT.dispose();
        bagT.dispose();
    }
    private void spawnNote(float y,float x,float h) {
        Rectangle note = new Rectangle();
        note.x = x;
        note.y = y;
        note.width = 128;
        note.height = h;
        notes.add(note);
        noteTs.add(RandomTex());
      //noteTs.add(noteT2);
    }
    private Texture RandomTex(){
        Random random = new Random();
        int i = random.nextInt(4);
        switch (i){
            case 0:
                tempT= appleT;
                break;
            case 1:
                tempT= canT;
            break;
            case 2:
                tempT= bagT;
            break;
            case 3:
                tempT= bananaT;
            break;
        }
        return tempT;
    }
    private void createPause(Stage stageS) {
        // Method to create and set up the pause menu

        // Create and set up the "Resume" button
        resume = new TextButton("Resume", skin);
        resume.getLabel().setFontScale(1 * Gdx.graphics.getDensity());
        resume.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);
        resume.setBounds(resume.getX(), Gdx.graphics.getHeight() / 2, resume.getLabel().getWidth(), resume.getLabel().getHeight());

        // Add listener to "Resume" button to resume the music and game
        resume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.play();
                RUNNING = true;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });


        // Create and set up the "Song Select" button
        songP = new TextButton("Song Select", skin);
        songP.getLabel().setFontScale(1 * Gdx.graphics.getDensity());
        songP.setPosition(resume.getX(), resume.getY() - 200);
        songP.setBounds(resume.getX(), resume.getY() - 200, resume.getLabel().getWidth(), resume.getLabel().getHeight());

        // Add listener to "Song Select" button to switch to the song picking screen
        songP.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SongPickingScreen(game, ""));
            }
        });

        // Create and set up the "Main Menu" button
        mainMenu = new TextButton("Main Menu", skin);
        mainMenu.getLabel().setFontScale(1 * Gdx.graphics.getDensity());
        mainMenu.setPosition(resume.getX(), resume.getY() - 400);
        mainMenu.setBounds(resume.getX(), resume.getY() - 400, resume.getLabel().getWidth(), resume.getLabel().getHeight());

        // Add listener to "Main Menu" button to go back to the main menu
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (myGameCallback != null) {
                    myGameCallback.goToMenu();
                } else {
                    Gdx.app.log("MyGame", "To use this class you must implement MyGameCallback!");
                }
            }
        });

        // Add the buttons to the stage
        stageS.addActor(songP);
        stageS.addActor(resume);
        stageS.addActor(mainMenu);
    }
    private void createEnd(Stage stageS) {
        // Method to create and set up the end game screen

        // Create and set up the "Continue" button
        end = new TextButton("Continue", skin);
        end.getLabel().setFontScale(1 * Gdx.graphics.getDensity());
        end.setPosition(resume.getX(), resume.getY());
        end.setBounds(resume.getX(), resume.getY() - 300, resume.getLabel().getWidth(), resume.getLabel().getHeight());

        // Add listener to "Continue" button to handle end of level logic
        end.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (myGameCallback != null) {
                    // Update the user's level if the current level is higher than the unlocked level
                    if (levelI > uLevel) {
                        uLevel = levelI;
                        myGameCallback.levelEnd(uLevel, Score, levelI);
                    } else {
                        myGameCallback.levelEnd(levelI, Score, levelI);
                    }
                } else {
                    Gdx.app.log("MyGame", "To use this class you must implement MyGameCallback!");
                }
            }
        });

        // Create and set up the "Clear!" label
        done = new Label("Clear!", skin);
        done.setPosition((Gdx.graphics.getWidth() / 8), 3 * (Gdx.graphics.getHeight() / 4));
        done.setFontScale(5);

        // Add the label and button to the stage
        stageS.addActor(done);
        stageS.addActor(end);
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

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Record the time when the touch down event occurs
        downT = timeSeconds;
        longPressed = false;

        // Convert screen coordinates to stage coordinates
        coord = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
        Actor hitButton = stage.hit(coord.x, coord.y, true);

        // Define rectangles for hitButton and notes for collision detection
        Rectangle rectangle = new Rectangle();
        rectangle.setPosition(coord.x, coord.y);
        rectangle.setSize(200, 200);
        Rectangle hitRect = new Rectangle();
        if (hitButton != null) hitRect.setPosition(hitButton.getX(), hitButton.getY());
        hitRect.setSize(200, 200);

        // Check if the touch event is not on the pause button
        if (hitButton != pause) {
            for (Rectangle note : notes) {
                // Check for collision between the touch area and notes
                if (rectangle.overlaps(hitRect) && hitRect.overlaps(note)) {
                    // Determine the timing of the hit and update score accordingly
                    if (note.getY() < hitButton.getY() + 200 && note.getY() > hitButton.getY() + 100) {
                        WriteLog("Perfect!");
                        TimingScore = "perfect!";
                        Score += 10;
                    } else if (note.getY() < hitButton.getY() + 100 && note.getY() > hitButton.getY()) {
                        WriteLog("Good!");
                        TimingScore = "good!";
                        Score += 5;
                    } else {
                        WriteLog("Missed!");
                        TimingScore = "missed!";
                    }
                    // Handle long notes and remove short notes
                    if (note.getHeight() > 128) {
                        isDeleted = false;
                        touchStart = timeSeconds;
                        removeNoteInd = notes.indexOf(note, false);
                    } else {
                        longs.removeIndex(notes.indexOf(note, false));
                        longInd.removeIndex(notes.indexOf(note, false));
                        noteTs.removeIndex(notes.indexOf(note, false));
                        notes.removeValue(note, true);
                    }
                }
            }
        }

        // Check if a button was touched and perform corresponding actions
        if (hitButton != null&&hitButton.getName()!="PB") {
            switch (hitButton.getName()) {
                case "t1":
                    key = "1";
                    break;
                case "t2":
                    key = "2";
                    break;
                case "t3":
                    key = "3";
                    break;
                case "t4":
                    key = "4";
                    break;
                case "pause":
                    // Pause the game and switch input processor to pause stage
                    RUNNING = false;
                    Gdx.input.setInputProcessor(pStage);
                    music.pause();
                    break;
            }
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        upT = timeSeconds;
        if(isDeleted==false) {
            touchEnd=timeSeconds;
            float timeTouched = touchEnd-touchStart;
            if(timeTouched>=longs.get(removeNoteInd)) {
                notes.removeIndex(removeNoteInd);
                noteTs.removeIndex(removeNoteInd);
                longInd.removeIndex(removeNoteInd);
                longs.removeIndex(removeNoteInd);
            }
        isDeleted=true;
        }
    //   FileHandle file2 = Gdx.files.local("test.txt");
     //  file2.writeString( key+ "," + (downT+0.2) + "-" + (upT - downT) + "\n", true);
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

    public void WriteLog(String str){
        Gdx.app.log("MyTag", str);
    }
}
