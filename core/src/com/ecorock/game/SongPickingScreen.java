package com.ecorock.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Sound;


public class SongPickingScreen implements Screen, InputProcessor {
    public interface MyGameCallback {
        public void goBack();
        public void changeFirst(boolean b);
    }
    final EcoRockGame game;
    private ScrollPane scroll;
    private FileHandle chosenSongBeat,chosenSong;
    private Stage stage,tStage;
    private Table LevelList;
    private Skin skin;
    private TextureAtlas atlas;
    private Vector2 coord;
    private Sound sound1;
    private GameScreen gameScreen;
    private Button btnBack,btnHelp;
    private InputMultiplexer multiplexer;
    private static MyGameCallback myGameCallback;
    private static int numberOfLevels;
    private TextButton end;
    private Label TutTXT;
    private String TutText="";
    private FileHandle fileHandle;
    private Texture BGT,ButtonT;
    private static boolean firstTime=true;
    private boolean helpB=false;
    public void setMyGameCallback(MyGameCallback callback) {
        myGameCallback = callback;
    }
    public SongPickingScreen(final EcoRockGame game){
        this.game = game;
    }
    public SongPickingScreen(final EcoRockGame game,String s){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        tStage = new Stage(new ScreenViewport());
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this);
        sound1 = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
        atlas = new TextureAtlas("ui/arcade-ui.atlas");
        skin = new Skin(Gdx.files.internal("ui/arcade-ui.json"),atlas);
        LevelList = new Table();
        btnBack = new Button(skin);
        btnHelp = new Button(skin);
        btnBack.setName("back");
        btnHelp.setName("help");
        scroll = new ScrollPane(LevelList);
        scroll.setScrollingDisabled(true,false);
        scroll.setFillParent(true);
        scroll.setName("scroll");
        BGT = new Texture(Gdx.files.internal("SongPickingBG.png"));
        ButtonT = new Texture(Gdx.files.internal("button_bg.png"));
        LevelList.left();
        createList(LevelList);
        Table rootUi = new Table();
        rootUi.setFillParent(true);
        rootUi.top();
        rootUi.add(btnHelp).size(200,200).align(Align.right).growX();
        rootUi.add(btnBack).size(200,200).align(Align.left).growX();
        stage.addActor(scroll);
        fileHandle = Gdx.files.internal("TutorialText.txt");
        TutText = fileHandle.readString();
        stage.addActor(rootUi);
        multiplexer.addProcessor(stage);
        chosenSongBeat = Gdx.files.internal("SongBeats/HisTheme.txt");
        chosenSong = Gdx.files.internal("Songs/Undertale OST_ 090 - His Theme.mp3");
        createTut(tStage);
    }
    public static void setNumberOfLevels(int l){
        numberOfLevels = l;
    }
    public static void setFirstTime(boolean f){firstTime=f;}
    public void createList(Table LevelList){
        int levelNum = 1;
        Label songName,author;
        Image lvlImage;

        Table temp;
        for (int i = 0; i < numberOfLevels+1; i++) {
            lvlImage = new Image(new Texture(Gdx.files.internal("LevelImages/Level " + levelNum +".png")));
            lvlImage.setName("level " + levelNum);
            LevelList.add(lvlImage).width(400).height(400);
        switch (levelNum) {
            case 1:
                temp = new Table();
                songName = new Label("His Theme", skin);
                author = new Label("Toby Fox", skin);
                author.setColor(Color.GRAY);
                songName.setName("label");
                author.setName("label");
               // songName.setFontScale(2 * Gdx.graphics.getDensity());
                //author.setFontScale(2);
                songName.setAlignment(Align.top);
                author.setAlignment(Align.top);
                temp.add(songName);
                temp.row();
                temp.add(author).align(Align.center);
                temp.setBackground(new TextureRegionDrawable(ButtonT));
                LevelList.add(temp).fillY().align(Align.left);
                LevelList.row();
                levelNum++;
                break;
            case 2:
                temp = new Table();
                songName = new Label("Littleroot Town", skin);
                author = new Label("Go Ichinose", skin);
                author.setColor(Color.GRAY);
                songName.setName("label");
                author.setName("label");
                //songName.setFontScale(1 * Gdx.graphics.getDensity());
                //author.setFontScale(2);
                songName.setAlignment(Align.top);
                author.setAlignment(Align.top);
                temp.add(songName);
                temp.row();
                temp.add(author).align(Align.center);
                temp.setBackground(new TextureRegionDrawable(ButtonT));
                LevelList.add(temp).fillY().align(Align.left);
                LevelList.row();
                levelNum++;
                break;
            case 3:
                temp = new Table();
                songName = new Label("The Legend Of Zelda", skin);
                author = new Label("Koji Kondo", skin);
                author.setColor(Color.GRAY);
                songName.setName("label");
                author.setName("label");
                //songName.setFontScale(1 * Gdx.graphics.getDensity());
                //author.setFontScale(2);
                songName.setAlignment(Align.top);
                author.setAlignment(Align.top);
                temp.add(songName);
                temp.row();
                temp.add(author).align(Align.center);
                temp.setBackground(new TextureRegionDrawable(ButtonT));
                LevelList.add(temp).fillY().align(Align.left);
                LevelList.row();
                levelNum++;
                break;
            case 4:
                temp = new Table();
                songName = new Label("Green Hill Zone", skin);
                author = new Label("Masato Nakamura", skin);
                author.setColor(Color.GRAY);
                songName.setName("label");
                author.setName("label");
                //songName.setFontScale(1 * Gdx.graphics.getDensity());
                //author.setFontScale(2);
                songName.setAlignment(Align.top);
                author.setAlignment(Align.top);
                temp.add(songName);
                temp.row();
                temp.add(author).align(Align.center);
                temp.setBackground(new TextureRegionDrawable(ButtonT));
                LevelList.add(temp).fillY().align(Align.left);
                LevelList.row();
                levelNum++;
                break;
            case 5:
                temp = new Table();
                songName = new Label("Jump Up, Super Star!", skin);
                author = new Label("Naoto Kubo", skin);
                author.setColor(Color.GRAY);
                songName.setName("label");
                author.setName("label");
                //songName.setFontScale(1 * Gdx.graphics.getDensity());
                //author.setFontScale(2);
                songName.setAlignment(Align.top);
                author.setAlignment(Align.top);
                temp.add(songName);
                temp.row();
                temp.add(author).align(Align.center);
                temp.setBackground(new TextureRegionDrawable(ButtonT));
                LevelList.add(temp).fillY().align(Align.left);
                LevelList.row();
                levelNum++;
                break;
            case 6:
                temp = new Table();
                songName = new Label("Il Vento D'oro", skin);
                author = new Label("Yugo Kanno", skin);
                author.setColor(Color.GRAY);
                songName.setName("label");
                author.setName("label");
                //songName.setFontScale(1 * Gdx.graphics.getDensity());
                //author.setFontScale(2);
                songName.setAlignment(Align.top);
                author.setAlignment(Align.top);
                temp.add(songName);
                temp.row();
                temp.add(author).align(Align.center);
                temp.setBackground(new TextureRegionDrawable(ButtonT));
                LevelList.add(temp).fillY().align(Align.left);
                LevelList.row();
                levelNum++;
                break;
        }
        }
        LevelList.row();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.getBatch().begin();
        stage.getBatch().draw(BGT,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.getBatch().end();
        if(firstTime||helpB){
            stage.act();
            stage.draw();
            stage.getBatch().begin();
            stage.getBatch().draw(ButtonT,0,end.getY(),Gdx.graphics.getWidth(),TutTXT.getHeight());
            stage.getBatch().end();
            tStage.act();
            tStage.draw();
        }
        else {
            stage.act();
            stage.draw();
        }
    }
    public void createTut(Stage stageS){
        end = new TextButton("Continue",skin);
        end.getLabel().setFontScale(1*Gdx.graphics.getDensity());
        end.setPosition(Gdx.graphics.getWidth()/2-4,Gdx.graphics.getHeight()/10);
        end.setBounds(Gdx.graphics.getWidth()/2-100,Gdx.graphics.getHeight()/10,end.getLabel().getWidth(),end.getLabel().getHeight());
        end.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                if(firstTime=true) {
                    firstTime = false;
                    myGameCallback.changeFirst(false);
                }
                if(helpB==true)
                {
                    helpB=false;
                }
            Gdx.input.setInputProcessor(multiplexer);
            }
        });
        TutTXT = new Label(TutText,skin);
        TutTXT.setPosition(0,end.getY()+100);
        TutTXT.setFontScale(Gdx.graphics.getHeight()/3000f);
        // Table root = new Table();
        //root.add(end);
        stageS.addActor(TutTXT);
        stageS.addActor(end);
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
    public void dispose() {
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
        coord = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
        Actor hitButton = stage.hit(coord.x, coord.y, true);
        Rectangle rectangle = new Rectangle();
        rectangle.setPosition(coord.x, coord.y);
        Rectangle hitRect = new Rectangle();
        if (hitButton != null) hitRect.setPosition(hitButton.getX(), hitButton.getY());
        hitRect.setSize(200, 200);
        rectangle.setSize(200, 200);
        if (hitButton != null) {
            switch (hitButton.getName()) {
                case "back" :{
                    if (myGameCallback != null) {
                        myGameCallback.goBack();
                    } else {
                        Gdx.app.log("MyGame", "To use this class you must implement MyGameCallback!");
                    }
                    break;
                }
                case "help":
                    helpB=true;
                    Gdx.input.setInputProcessor(tStage);
                    break;
                case "level 1":
                    chosenSong = Gdx.files.internal("Songs/Undertale OST_ 090 - His Theme.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/HisTheme.txt");
                    gameScreen = new GameScreen(game, chosenSongBeat, chosenSong,1,numberOfLevels+1);
                    game.setScreen(gameScreen);
                    break;
                case "level 2":
                    chosenSong = Gdx.files.internal("Songs/Pokemon Emerald Soundtrack #5 - Littleroot Town.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/Littleroot.txt");
                    gameScreen = new GameScreen(game, chosenSongBeat, chosenSong,2,numberOfLevels+1);
                    game.setScreen(gameScreen);
                    break;
                case "level 3":
                    chosenSong = Gdx.files.internal("Songs/Zelda Main Theme Song.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/TLOZ.txt");
                    gameScreen = new GameScreen(game, chosenSongBeat, chosenSong,2,numberOfLevels+1);
                    game.setScreen(gameScreen);
                    break;
                case "level 4":
                    chosenSong = Gdx.files.internal("Songs/Sonic The Hedgehog OST - Green Hill Zone.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/GreenHillZone.txt");
                    gameScreen = new GameScreen(game, chosenSongBeat, chosenSong,2,numberOfLevels+1);
                    game.setScreen(gameScreen);
                    break;
                case "level 5":
                    chosenSong = Gdx.files.internal("Songs/Super Mario Odyssey - Jump Up, Super Star!.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/Littleroot.txt");
                    gameScreen = new GameScreen(game, chosenSongBeat, chosenSong,2,numberOfLevels+1);
                    game.setScreen(gameScreen);
                    break;
                case "level 6":
                    chosenSong = Gdx.files.internal("Songs/il vento d'oro.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/Littleroot.txt");
                    gameScreen = new GameScreen(game, chosenSongBeat, chosenSong,2,numberOfLevels+1);
                    game.setScreen(gameScreen);
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
