package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;
import com.mobilelearning.konnecting.accessors.ActorAccessor;
import com.mobilelearning.konnecting.accessors.CellAccessor;
import com.mobilelearning.konnecting.accessors.LabelAccessor;
import com.mobilelearning.konnecting.uiUtils.AnimatedActor;
import com.mobilelearning.konnecting.uiUtils.AnimationDrawable;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created with IntelliJ IDEA.
 * User: AFFonseca
 * Date: 02-03-2015
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public abstract class StandardScreen extends ScreenAdapter {
    private String loadingHints [] = {""};

    protected KonnectingGame game;

    private final static int width = 640, height = 960;

    protected final StandardStage mainStage;
    protected final Stage backgroundStage;
    public Skin uiSkin;
    protected final TweenManager tweenManager;

    private boolean loadingNextScreen;
    private Table loadingScreen;
    private Label loadingScreenLabel;
    private boolean [] actorsState;
    protected InputMultiplexer inputMultiplexer;

    private static final ExtendViewport stageViewport = new ExtendViewport(width,height);
    private static final FillViewport backgroundViewport = new FillViewport(width,height);

    public StandardScreen(KonnectingGame game, SpriteBatch batch){
        this.game = game;
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());
        Tween.registerAccessor(Label.class, new LabelAccessor());
        Tween.registerAccessor(Cell.class, new CellAccessor());
        backgroundStage = new Stage(backgroundViewport, batch);
        mainStage = new StandardStage(stageViewport, batch, width, height);
        inputMultiplexer = new InputMultiplexer(mainStage, backgroundStage);
        uiSkin = Assets.uiSkin;

        createLoadingScreen();

    }

    private void createLoadingScreen(){
        loadingNextScreen = false;

        loadingScreen = new Table();
        loadingScreen.setSize(mainStage.getWidth(), mainStage.getHeight());
        loadingScreen.bottom().padBottom(20f);

        AnimatedActor loadingAnimation = new AnimatedActor(new AnimationDrawable(Assets.loading));
        loadingAnimation.setPosition(
                (mainStage.getWidth() - loadingAnimation.getPrefWidth()) / 2,
                (mainStage.getHeight() - loadingAnimation.getPrefHeight()) / 2);
        loadingScreen.addActor(loadingAnimation);

        loadingScreenLabel = new Label("", new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE));
        loadingScreenLabel.setFontScale(0.75f); loadingScreenLabel.setWrap(true);
        loadingScreenLabel.setAlignment(Align.center);

        loadingScreenLabel.getStyle().background = new TextureRegionDrawable(
                new TextureRegion(Assets.loading.getKeyFrame(0),
                        0, (int)loadingAnimation.getPrefHeight()/10, 1, 1));

        loadingScreen.add(loadingScreenLabel).width(mainStage.getWidth() - 50f);

        loadingScreen.setVisible(false);
        mainStage.addActor(loadingScreen);
        mainStage.setLastOverlayActor(loadingScreen);

    }

    public void startLoadingAnimation(boolean loadingNextScreen){
        this.loadingNextScreen = loadingNextScreen;

        Gdx.input.setInputProcessor(null);

        if(loadingNextScreen) {
            Array<Actor> actors = mainStage.getActors();
            actorsState = new boolean[actors.size];
            for (int i = 0; i < mainStage.getActors().size; i++) {
                actorsState[i] = actors.get(i).isVisible();
                actors.get(i).setVisible(false);
            }
            loadingScreenLabel.setText(loadingHints[MathUtils.random(loadingHints.length - 1)]);
        }
        else {
            actorsState = null;
        }

        loadingScreen.setVisible(true);
    }

    public void stopLoadingAnimation(){
        this.loadingNextScreen = false;

        if(actorsState != null)
        {
            for(int i=0; i< mainStage.getActors().size; i++){
                mainStage.getActors().get(i).setVisible(actorsState[i]);
            }
        }

        Gdx.input.setInputProcessor(inputMultiplexer);
        loadingScreen.setVisible(false);
        loadingScreenLabel.setText("");

    }

    protected void updateUI(float delta){
        backgroundStage.getViewport().apply();
        backgroundStage.act(delta);
        backgroundStage.draw();

        mainStage.getViewport().apply();
        mainStage.act(delta);
        mainStage.draw();

        tweenManager.update(delta);
    }

    @Override
    public void render (float delta){
        if(loadingNextScreen && Assets.update())
            game.changeScreen(StandardScreen.this);

        updateUI(delta);
    }

    @Override
    public void resize (int width, int height) {
        mainStage.getViewport().update(width, height, true);
        backgroundStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        mainStage.dispose();
        backgroundStage.dispose();
    }

    public abstract void load();

    public void prepare(){
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public abstract void unload();

    //This stage has 2 different capabilities:
    //1 - Using an extended view it works similar to a fit view without black bars.
    //2 - Actors can be defined to always be on top. This is used with the loading and exit buttons
    public class StandardStage extends Stage {
        private Actor lastOverlayActor;
        protected float realWidth, realHeight;
        private float padLeft, padBottom;

        public StandardStage(Viewport viewport, Batch batch, float realWidth, float realHeight) {
            super(viewport, batch);
            this.realWidth = realWidth;
            this.realHeight = realHeight;
            this.padLeft = (super.getWidth()-realWidth)/2;
            this.padBottom = (super.getHeight()-realHeight)/2;
            getRoot().setPosition(padLeft, padBottom);
            getRoot().setCullingArea(new Rectangle(-padLeft, -padBottom, super.getWidth(), super.getHeight()));
        }

        public void setLastOverlayActor(Actor actor){
            lastOverlayActor = actor;
        }

        @Override
        public void addActor(Actor actor) {
            if(lastOverlayActor == null)
                super.addActor(actor);
            else
                getRoot().addActorBefore(lastOverlayActor, actor);
        }

        @Override
        public float getWidth() {
            return realWidth;
        }

        public  float getRealWidth(){
            return super.getWidth();
        }

        @Override
        public float getHeight() {
            return  realHeight;
        }

        public  float getRealHeight(){
            return super.getHeight();
        }

        public float getPadLeft(){
            return padLeft;
        }

        public float getPadBottom(){
            return padBottom;
        }

        public TweenManager getTweenManager(){
            return tweenManager;
        }

        @Override
        public void clear() {
            if(lastOverlayActor == null){
                super.clear();
                return;
            }

            SnapshotArray<Actor> root = getRoot().getChildren();
            while (root.get(0)!=lastOverlayActor && root.size != 0){
                unfocus(root.get(0));
                root.get(0).remove();
            }
        }

        @Override
        public void unfocusAll() {
            if(lastOverlayActor == null){
                super.unfocusAll();
                return;
            }

            SnapshotArray<Actor> root = getRoot().getChildren();
            for(int i=0; root.get(i)!=lastOverlayActor && i<root.size; i++){
                unfocus(root.get(i));
            }
        }

        @Override
        public void dispose() {
            super.unfocusAll();
            getRoot().clear();
        }

    }
}
