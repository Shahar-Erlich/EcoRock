package com.ecorock.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Sound;


public class SongPickingScreen implements Screen, InputProcessor {
    final EcoRockGame game;
    private ScrollPane scroll;
    private FileHandle chosenSongBeat,chosenSong;
    private Stage stage;
    private Table LevelList;
    private Skin skin;
    private TextureAtlas atlas;
    private Vector2 coord;
    private Sound sound1;
    private final int NumberOfLevels =2;
    public SongPickingScreen(final EcoRockGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);
        sound1 = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
        atlas = new TextureAtlas("ui/arcade-ui.atlas");
        skin = new Skin(Gdx.files.internal("ui/arcade-ui.json"),atlas);
        LevelList = new Table();
        scroll = new ScrollPane(LevelList);
        scroll.setScrollingDisabled(true,false);
        scroll.setFillParent(true);
        scroll.setName("scroll");
        LevelList.left();
        createList(LevelList);
        stage.addActor(scroll);
        chosenSongBeat = Gdx.files.internal("SongBeats/HisTheme.txt");
        chosenSong = Gdx.files.internal("Songs/Undertale OST_ 090 - His Theme.mp3");
    }
    public void createList(Table LevelList){
        int levelNum = 1;
        Label songName,author;
        Image lvlImage;

        Table temp;
        for (int i = 1; i < NumberOfLevels+1; i++) {
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
                LevelList.add(temp).fillY().align(Align.left);
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
            stage.act();
            stage.draw();
            dispose();
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
                case "level 1":
                    chosenSong = Gdx.files.internal("Songs/Undertale OST_ 090 - His Theme.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/HisTheme.txt");
                    game.setScreen(new GameScreen(game, chosenSongBeat, chosenSong));
                    //sound1.play();
                    dispose();
                    break;
                case "level 2":
                    chosenSong = Gdx.files.internal("Songs/Pokemon Emerald Soundtrack #5 - Littleroot Town.mp3");
                    chosenSongBeat = Gdx.files.internal("SongBeats/Littleroot.txt");
                    game.setScreen(new GameScreen(game, chosenSongBeat, chosenSong));
                    dispose();
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
