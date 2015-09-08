package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.mobilelearning.konnecting.accessors.LabelAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;

/**
 * Created by AFFonseca on 27/08/2015.
 */
public class WelcomeScreen extends StandardScreen  {
    private final static String [][] alienTalk = {
            {"Olá\n" +
                "Sou, como vocês dizem, um extraterrestre. Chamo-me Komuniket. Preciso de ajuda " +
                "para fazer um relatório completo, explicando como vocês comunicam...\n" +
                "Por isso, invadi o seu dispositivo móvel! Viajei ao longo do vosso tempo e trouxe umas imagens " +
                "que necessito que me explique.\n" +
                "Pode ajudar-me?"},
            {"Ok. Então vamos começar!", "A sério? Vou perguntar mais uma vez...\n\nQuer ajudar-me na minha aventura?"},
            {"Eu sabia que não me ia desapontar. Vamos começar!", "Oh... ok então. Até uma próxima.\n\nAdeus!"}
    };
    private TextureAtlas atlas;
    private Label text;
    private Image alien;
    private int currentConversation = 0, alienHappiness = 2;
    private TextButton yesButton, noButton;

    public WelcomeScreen(KonnectingGame game, SpriteBatch batch) {
        super(game, batch);
    }

    @Override
    public void load() {
        Assets.loadWelcomeScreen();
    }

    @Override
    public void prepare() {
        super.prepare();
        atlas = Assets.prepareWelcomeScreen();
    }


    @Override
    public void unload() {
        Assets.unloadWelcomeScreen();
    }

    @Override
    public void show() {
        super.show();

        backgroundStage.addActor(new Image(atlas.findRegion("background")));

        Image alienFinger = new Image(atlas.findRegion("alien_finger"));
        alienFinger.setOrigin(alienFinger.getPrefWidth(), 0f);
        alienFinger.setScale(0.9f);
        alienFinger.setPosition(mainStage.getWidth() + mainStage.getPadLeft(), -286f);
        mainStage.addActor(alienFinger);

        alien = new Image(atlas.findRegion("alien_happy"));
        alien.setPosition(-423f, -alien.getHeight() - mainStage.getPadBottom());
        mainStage.addActor(alien);

        Table container = new Table();
        container.setBackground(new TextureRegionDrawable(atlas.findRegion("text_panel")));
        container.setBounds(45f, 531f, container.getBackground().getMinWidth(),
                container.getBackground().getMinHeight());
        container.padBottom(23f); container.getColor().a = 0f;

        text = new Label("", new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE));
        text.setName(alienTalk[0][0]); text.setAlignment(Align.center);
        text.setFontScale(0.45f); text.setWrap(true);
        container.add(text).width(container.getWidth() - 30f);
        mainStage.addActor(container);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = uiSkin.getFont("default-font");
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        textButtonStyle.down = new TextureRegionDrawable(atlas.findRegion("button_down"));

        yesButton = new TextButton("sim", textButtonStyle);
        yesButton.getLabel().setFontScale(0.6f); yesButton.setVisible(false);
        yesButton.setPosition(45f, 346f); mainStage.addActor(yesButton);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                continueConversation(true);
            }
        });

        noButton = new TextButton("não", textButtonStyle);
        noButton.getLabel().setFontScale(0.6f); noButton.setVisible(false);
        noButton.setPosition(45f, 232f); mainStage.addActor(noButton);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                continueConversation(false);
            }
        });

        Timeline.createSequence()
                .pushPause(1f)
                .push(Tween.to(alienFinger, ActorAccessor.MOVE_X, 1f).target(166f))
                .pushPause(0.25f)
                .push(Tween.to(alienFinger, ActorAccessor.SCALEXY, 0.25f).target(1f).ease(Linear.INOUT))
                .push(Tween.to(alienFinger, ActorAccessor.SCALEXY, 0.25f).target(0.9f).ease(Linear.INOUT))
                .push(Tween.to(alienFinger, ActorAccessor.SCALEXY, 0.25f).target(1f).ease(Linear.INOUT))
                .push(Tween.to(alienFinger, ActorAccessor.SCALEXY, 0.25f).target(0.9f).ease(Linear.INOUT))
                .pushPause(0.5f)
                .push(Tween.to(alienFinger, ActorAccessor.MOVE_X, 1f)
                        .target(mainStage.getWidth() + mainStage.getPadLeft()))
                .beginParallel()
                .push(Tween.to(alien, ActorAccessor.MOVE_Y, 1f)
                        .target(-alien.getHeight() + 519f))
                .push(Tween.to(container, ActorAccessor.ALPHA, 1f).target(1f))
                .end()
                .push(Tween.to(text, LabelAccessor.SCROLL, text.getName().length()/20).target(1f).ease(Linear.INOUT))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        yesButton.setVisible(true);
                        noButton.setVisible(true);
                    }
                })
                .start(tweenManager);

    }

    private void continueConversation(final boolean yes){
        yesButton.setVisible(false);
        noButton.setVisible(false);

        text.setText("");
        currentConversation++;
        if(yes) {
            text.setName(alienTalk[currentConversation][0]);
            alienHappiness++;
        }
        else{
            text.setName(alienTalk[currentConversation][1]);
            alienHappiness--;
        }

        switch (alienHappiness){
            case 0:
                alien.setDrawable(new TextureRegionDrawable(atlas.findRegion("alien_sad")));
                break;
            case 1:
                alien.setDrawable(new TextureRegionDrawable(atlas.findRegion("alien_normal")));
                break;
            default:
                alien.setDrawable(new TextureRegionDrawable(atlas.findRegion("alien_happy")));
        }

        Tween.to(text, LabelAccessor.SCROLL, text.getName().length()/20).target(1f).ease(Linear.INOUT)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (yes) {
                            Timeline.createSequence()
                                    .pushPause(1.5f)
                                    .setCallback(new TweenCallback() {
                                        @Override
                                        public void onEvent(int type, BaseTween<?> source) {
                                            SavedData.setGameProgress(1);
                                            game.loadNextScreen(WelcomeScreen.this,
                                                    KonnectingGame.ScreenType.KRONOS_MENU);
                                        }
                                    }).start(tweenManager);
                        } else if (currentConversation == alienTalk.length - 1) {
                            Timeline.createSequence()
                                    .pushPause(1.5f)
                                    .setCallback(new TweenCallback() {
                                        @Override
                                        public void onEvent(int type, BaseTween<?> source) {
                                            Gdx.app.exit();
                                        }
                                    }).start(tweenManager);

                        } else {
                            yesButton.setVisible(true);
                            noButton.setVisible(true);
                        }
                    }
                }).start(tweenManager);
    }
}
