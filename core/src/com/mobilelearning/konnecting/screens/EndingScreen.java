package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;
import com.mobilelearning.konnecting.SavedData;
import com.mobilelearning.konnecting.accessors.ActorAccessor;
import com.mobilelearning.konnecting.serviceHandling.json.StatsData;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by AFFonseca on 01/09/2015.
 */
public class EndingScreen extends StandardScreen {
    private TextureAtlas atlas;
    private static final String comments [] = {
            "Prestou uma Excelente colaboração para a comunicação intergaláctica.",
            "Prestou uma Boa ajuda para a comunicação intergaláctica.",
            "Não quer rever os seus conhecimentos sobre a comunicação humana?",
    };

    private static final int scoreLimits [] = {0, 2001, 2901};


    public EndingScreen(KonnectingGame game, SpriteBatch batch) {
        super(game, batch);
    }

    @Override
    public void load() {
        Assets.loadEndingScreen();
    }

    @Override
    public void prepare() {
        super.prepare();
        atlas = Assets.prepareEndingScreen();
    }

    @Override
    public void unload() {
        Assets.unloadEndingScreen();
    }

    @Override
    public void show() {
        super.show();

        backgroundStage.addActor(new Image(atlas.findRegion("background")));

        Image alien = new Image(atlas.findRegion("alien"));
        alien.setPosition(mainStage.getWidth() + mainStage.getPadLeft(), -365f);
        mainStage.addActor(alien);

        Image energy = new Image(atlas.findRegion("energy")); //195, 313
        energy.setPosition(83f, 201f); energy.setScale(0f);
        energy.setOrigin(energy.getPrefWidth() / 2, energy.getPrefHeight() / 2);
        mainStage.addActor(energy);

        final Table certificateTable = new Table();
        certificateTable.padTop(314f).padBottom(101f).defaults().padBottom(100f);
        certificateTable.setBackground(new TextureRegionDrawable(atlas.findRegion("certificate")));
        certificateTable.setSize(mainStage.getWidth(), mainStage.getHeight());
        certificateTable.setOrigin(195f, 313f); certificateTable.setTransform(true);
        certificateTable.setTouchable(Touchable.disabled);
        certificateTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.fanfareFX.stop();
                game.loadNextScreen(EndingScreen.this, KonnectingGame.ScreenType.MAIN_MENU);
            }
        });
        certificateTable.setScale(0f);
        mainStage.addActor(certificateTable);

        StatsData tempData = SavedData.getStats();
        int score = tempData.kronosScore1 + tempData.kronosScore2 + tempData.kronosScore3
                + tempData.kronosScore4 + tempData.kronosScore5 + tempData.kronosScore6
                + tempData.kronosScore7 + tempData.zappingScore1 + tempData.zappingScore2
                + tempData.zappingScore3 + tempData.zappingScore4 + tempData.zappingScore5;

        final Label userLabel = new Label(SavedData.getUsername() +"\n\n" +score +"pt",
                new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE));
        userLabel.setWrap(true); userLabel.setAlignment(Align.center);
        userLabel.getColor().a = 0f;
        certificateTable.add(userLabel).width(407f).row();

        String correctComment = comments[0];
        for(int i=scoreLimits.length-1; i>=0; i--){
            if(score > scoreLimits[i]){
                correctComment = comments[i];
                break;
            }
        }
        final Label commentLabel = new Label(correctComment,
                new Label.LabelStyle(uiSkin.getFont("arial"), Color.WHITE));
        commentLabel.setWrap(true); commentLabel.setFontScale(0.9f);
        commentLabel.setAlignment(Align.center);
        commentLabel.getColor().a = 0f;
        certificateTable.add(commentLabel).width(407f);


        Timeline.createSequence()
                .push(Tween.to(alien, ActorAccessor.MOVE_X, 1f).target(20f))
                .pushPause(0.25f)
                .push(Tween.to(energy, ActorAccessor.SCALEXY, 1f).target(1f))
                .push(Tween.to(energy, ActorAccessor.SCALEXY, 0.5f).target(0.8f))
                .push(Tween.to(energy, ActorAccessor.SCALEXY, 0.5f).target(1f))
                .push(Tween.to(certificateTable, ActorAccessor.SCALEXY, 0.5f).target(1f))
                .beginParallel()
                .push(Tween.to(userLabel, ActorAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(commentLabel, ActorAccessor.ALPHA, 0.5f).target(1f))
                .end()
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Assets.fanfareFX.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                Assets.fanfareFX.setOnCompletionListener(null);
                                certificateTable.setTouchable(Touchable.enabled);
                            }
                        });
                        Assets.fanfareFX.play();
                    }
                })
                .start(tweenManager);
    }
}
