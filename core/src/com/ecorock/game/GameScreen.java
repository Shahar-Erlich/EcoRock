package com.ecorock.game;

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

public class GameScreen implements Screen, InputProcessor {
    public interface MyGameCallback {
        public void goToMenu();
        public void levelEnd(int level,int score,int i);
    }

    final EcoRockGame game;
    // Local variable to hold the callback implementation
    private static MyGameCallback myGameCallback;
    private Sound sound1,sound2,sound3,sound4;
    Stage stage,pStage,eStage;
    private Array<Rectangle> notes;
    private Vector2 coord;
    private  boolean RUNNING,isDeleted=true,longPressed=false,ended=false;
    private Button touch1,touch2,touch3,touch4, pause;
    private Texture noteT,noteT2,BGT,ButtonT;
    private Music music;
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
    private static String texturePath="GuitarNeckTest.png";
    private Texture tempT;
    private float fadeTime=1;
    private int levelI,uLevel;
    public boolean endTime=false;

    public void setMyGameCallback(MyGameCallback callback) {
        myGameCallback = callback;
    }
    public GameScreen(final EcoRockGame game){this.game = game;}

    public GameScreen(final EcoRockGame gam,FileHandle chosenSongBeat,FileHandle chosenSong,int levelNum,int userLevel){
        notesMissed=0;
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

        rootUi.add(progressBar).growX();
        rootUi.row();
        int space =45;
        rootUi.add(pause).size(200,200).growX().align(Align.right);
    //rootUi.add(ScoreL);
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
    }
    @Override
    public void show() {

    }
    public void createNotes(){
        float posI=0;
        double height=128;
        stage.act();
        stage.draw();
        for (int i = 0; i < secs.length; i++) {
            height=128;
            switch ((pos[i])){
                case 1:
                    Vector2 t1C = touch1.localToStageCoordinates(new Vector2(0,0));
                    posI = t1C.x;
                    break;
                case 2:
                    Vector2 t2C = touch2.localToStageCoordinates(new Vector2(0,0));
                    posI = t2C.x;
                    break;
                case 3:
                    Vector2 t3C = touch3.localToStageCoordinates(new Vector2(0,0));
                    posI = t3C.x;
                    break;
                case 4:
                    Vector2 t4C = touch4.localToStageCoordinates(new Vector2(0,0));
                    posI = t4C.x;
                    break;

            }
            if(longs.get(i)!=0 && longs.get(i)*600>150){
                height = Math.floor(longs.get(i)*600);
                longInd.add(1);
            }
            else{
                longInd.add(0);
            }
        spawnNote(secs[i]*600,posI,(float)height);
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        if(music.getVolume()<=0){
            if(ended==false){
                multiplexer.addProcessor(eStage);
            }
            stage.getBatch().begin();
            stage.getBatch().draw(BGT,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            stage.getBatch().draw(noteT,0,resume.getY()-400,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2);
            stage.getBatch().end();
            ended=true;
            RUNNING=false;
            music.stop();
            eStage.act();
            eStage.draw();

        }
        if(RUNNING){
            timeSeconds +=Gdx.graphics.getDeltaTime();
        }
        if(timeSeconds>6&&music.isPlaying()){
            fadeTime-= delta * 0.7;
            music.setVolume(fadeTime);
        }
         if(notesMissed==1){
           music.stop();
          game.setScreen(new SongPickingScreen(game,""));
          return;
       }
            if(RUNNING) {
                stage.getBatch().begin();
                stage.getBatch().draw(BGT,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                stage.getBatch().end();
                progressBar.setValue((timeSeconds/120)*100);
                stage.act();
                stage.draw();
                if(isDeleted==false){
                    noteH=notes.get(removeNoteInd).getHeight();
                    noteY=notes.get(removeNoteInd).getY();
                    notes.get(removeNoteInd).setY(noteY+600*Gdx.graphics.getDeltaTime());
                    notes.get(removeNoteInd).setHeight(noteH-600*Gdx.graphics.getDeltaTime());
                    Score+=1;
                    if(notes.get(removeNoteInd).getHeight()<=1){
                        isDeleted=true;
                        notes.removeIndex(removeNoteInd);
                        longs.removeIndex(removeNoteInd);
                        longInd.removeIndex(removeNoteInd);
                        noteTs.removeIndex(removeNoteInd);
                    }
                }
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
                game.batch.begin();
                game.font.draw(game.batch, String.valueOf(Score),(Gdx.graphics.getWidth()/4),Gdx.graphics.getHeight());
               // game.font.draw(game.batch, temp, 0, Gdx.graphics.getHeight());
                for (Rectangle note : notes) {
                    if(longInd.get(notes.indexOf(note,true))==1){
                        game.batch.draw(noteT, note.x, note.y,200, note.height);
                        game.batch.draw(noteTs.get(notes.indexOf(note,false)), note.x, note.y, 200, 200);
                    }
                    else{
                        game.batch.draw(noteTs.get(notes.indexOf(note,false)), note.x, note.y, 200, 200);
                    }
                }
               // game.batch.draw(ButtonT,Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()-200,200,200);
                game.batch.end();
            }
            else if(ended==false){
                game.batch.begin();
                for (Rectangle note : notes) {
                    game.batch.draw(noteT, note.x, note.y, note.width, note.height);
                }
                game.batch.end();
                music.pause();
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
    }
    private void spawnNote(float y,float x,float h) {
        Rectangle note = new Rectangle();
        note.x = x;
        note.y = y;
        note.width = 128;
        note.height = h;
        notes.add(note);
        noteTs.add(RandomTex());
    }
    private Texture RandomTex(){
        Random random = new Random();
        int i = random.nextInt(4);
        tempT= new Texture(Gdx.files.internal("apple.png"));
        switch (i){
            case 0:
                tempT= new Texture(Gdx.files.internal("apple.png"));
                break;
            case 1:
                tempT= new Texture(Gdx.files.internal("can.png"));
            break;
            case 2:
                tempT= new Texture(Gdx.files.internal("tBag.png"));
            break;
            case 3:
                tempT= new Texture(Gdx.files.internal("banana.png"));
            break;
        }
        return tempT;
    }
    private void createPause(Stage stageS){
        resume = new TextButton("Resume",skin);
        resume.getLabel().setFontScale(1*Gdx.graphics.getDensity());
        resume.setPosition(Gdx.graphics.getWidth()/2-100,Gdx.graphics.getHeight()/2);
        resume.setBounds(resume.getX(),Gdx.graphics.getHeight()/2,resume.getLabel().getWidth(),resume.getLabel().getHeight());
        //music de-syncs
        resume.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                music.play();
                RUNNING=true;
                Gdx.input.setInputProcessor(multiplexer);
            }
             });

        songP = new TextButton("Song Select",skin);
        songP.getLabel().setFontScale(1*Gdx.graphics.getDensity());
        songP.setPosition(resume.getX(),resume.getY()-200);
        songP.setBounds(resume.getX(),resume.getY()-200,resume.getLabel().getWidth(),resume.getLabel().getHeight());
        songP.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                game.setScreen(new SongPickingScreen(game,""));
            }
        });
        mainMenu = new TextButton("Main Menu",skin);
        mainMenu.getLabel().setFontScale(1*Gdx.graphics.getDensity());
        mainMenu.setPosition(resume.getX(),resume.getY()-400);
        mainMenu.setBounds(resume.getX(),resume.getY()-400,resume.getLabel().getWidth(),resume.getLabel().getHeight());
        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                if (myGameCallback != null) {
                        myGameCallback.goToMenu();
                } else {
                    Gdx.app.log("MyGame", "To use this class you must implement MyGameCallback!");
                }
                Gdx.app.log("Tag","asfasdf");
            }
        });
        stageS.addActor(songP);
        stageS.addActor(resume);
        stageS.addActor(mainMenu);
    }
    private void createEnd(Stage stageS){

        end = new TextButton("Continue",skin);
        end.getLabel().setFontScale(1*Gdx.graphics.getDensity());
        end.setPosition(resume.getX(),resume.getY());
        end.setBounds(resume.getX(),resume.getY()-300,resume.getLabel().getWidth(),resume.getLabel().getHeight());
        end.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                if (myGameCallback != null) {
                    if(levelI>uLevel){
                        uLevel=levelI;
                        myGameCallback.levelEnd(uLevel,Score,levelI);
                    }
                    else{
                        myGameCallback.levelEnd(levelI,Score,levelI);
                    }

                } else {
                    Gdx.app.log("MyGame", "To use this class you must implement MyGameCallback!");
                }
                Gdx.app.log("Tag","asfasdf");
            }
        });
        done = new Label("Clear!",skin);
        done.setPosition((Gdx.graphics.getWidth()/8),3*(Gdx.graphics.getHeight()/4));
        done.setFontScale(5);
       // Table root = new Table();
        //root.add(end);
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        downT = timeSeconds;
        longPressed=false;
       // Gdx.app.log("Tag",pointer + "");
        coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitButton = stage.hit(coord.x, coord.y, true);
        Rectangle rectangle = new Rectangle();
        rectangle.setPosition(coord.x,coord.y);
        Rectangle hitRect = new Rectangle();
        if(hitButton!=null)hitRect.setPosition(hitButton.getX(),hitButton.getY());
        hitRect.setSize(200,200);
        rectangle.setSize(200,200);
        if(hitButton!=pause) {
            for (Rectangle note : notes) {
                if (rectangle.overlaps(hitRect) && hitRect.overlaps(note)) {
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
                    if (note.getHeight() > 128) {
                        isDeleted = false;
                     //   longPressed=true;
                        touchStart = timeSeconds;
                        removeNoteInd = notes.indexOf(note, false);
                    } else {
                        longs.removeIndex(notes.indexOf(note, false));
                        longInd.removeIndex(notes.indexOf(note,false));
                        noteTs.removeIndex(notes.indexOf(note,false));
                        notes.removeValue(note, true);
                    }
                    //  Gdx.app.log("MyTag", "Hit!");
                }
            }
        }
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
        //FileHandle file2 = Gdx.files.local("test.txt");
        //file2.writeString( key+ "," + (downT+0.2) + "-" + (upT - downT) + "\n", true);
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
