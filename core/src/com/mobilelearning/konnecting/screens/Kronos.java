package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;
import com.mobilelearning.konnecting.SavedData;
import com.mobilelearning.konnecting.accessors.ActorAccessor;
import com.mobilelearning.konnecting.accessors.LabelAccessor;
import com.mobilelearning.konnecting.serviceHandling.json.StatsData;
import com.mobilelearning.konnecting.uiUtils.TopTable;
import com.mobilelearning.konnecting.uiUtils.UpdateStatsDialog;

import java.util.concurrent.atomic.AtomicInteger;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;

/**
 * Created by AFFonseca on 28/08/2015.
 */
public class Kronos extends StandardScreen {
    int[] exerciseSize = {7, 13, 8, 17, 13, 11, 13};
    private final int timerMaxTime = 60;
    private int kronosNumber, exercisePad, currentExercise = 0, scoreCounter = 0;
    private ExerciseInfo exercises [];

    private Table mainTable, timelineTable;
    private TextureAtlas atlas, titleImagesAtlas, timelineImagesAtlas;
    private Image mainImage;
    private Label optionLabels [], timer;
    private Button selectedOptionsButtons[], infoButton, acceptButton;
    private Label selectedOptionsLabels[];
    private Button timelineLeft, timelineRight, optionsLeft, optionsRight;
    private ScrollPane imagesPane, optionsPane;
    private Image timelineImages[];
    private Table infoTable;
    private Image infoImage, fingerImage = null;
    private TopTable topTable;
    private Label infoTitle, infoComment;


    public Kronos(KonnectingGame game, SpriteBatch batch, int kronosNumber) {
        super(game, batch);
        this.kronosNumber = kronosNumber -1;

        exercises = new Kronos.ExerciseInfo[exerciseSize[kronosNumber -1]];
        exercisePad = 0;
        for(int i=0; i< kronosNumber -1; i++){
            exercisePad += exerciseSize[i];
        }
        System.arraycopy(Assets.exercisesData.infos, exercisePad, exercises, 0, exercises.length);
    }

    @Override
    public void load() {
    }

    @Override
    public void prepare() {
        super.prepare();
        TextureAtlas [] atlases = Assets.prepareKronos();
        atlas = atlases[0]; titleImagesAtlas = atlases[1]; timelineImagesAtlas = atlases[2];
    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {
        super.show();

        backgroundStage.addActor(new Image(atlas.findRegion("background")));

        mainTable = new Table();
        mainTable.setSize(mainStage.getWidth(), mainStage.getHeight());
        mainStage.addActor(mainTable);

        Image imagePanel = new Image(atlas.findRegion("image_panel"));
        imagePanel.setPosition((mainStage.getWidth() - imagePanel.getPrefWidth()) / 2, 412f);
        mainTable.addActor(imagePanel);

        mainImage = new Image(titleImagesAtlas.findRegion("" +(exercisePad+1)));
        mainImage.setPosition((mainStage.getWidth() - mainImage.getPrefWidth()) / 2, 588f);
        mainImage.setOrigin(mainImage.getPrefWidth() / 2, mainImage.getPrefHeight() / 2);
        mainImage.setScaleY(0f);
        mainTable.addActor(mainImage);

        final Table optionsTable = new Table();
        optionsTable.padLeft(6f).padRight(6f);
        optionsTable.setBounds(0f, 481f, mainStage.getWidth(), 55f);

        optionsLeft = new Button(
                new TextureRegionDrawable(atlas.findRegion("button_back_up")),
                new TextureRegionDrawable(atlas.findRegion("button_back_down"))
        );

        optionsRight = new Button(
                new TextureRegionDrawable(atlas.findRegion("button_forward_up")),
                new TextureRegionDrawable(atlas.findRegion("button_forward_down"))
        );

        Table innerOptionTable = new Table();
        optionLabels = new Label[6];
        for(int i=0; i<optionLabels.length; i++) {
            optionLabels[i] = new Label("",
                    new Label.LabelStyle(uiSkin.getFont("arial"), Color.WHITE));
            optionLabels[i].setAlignment(Align.center);
            optionLabels[i].setOrigin(253f, 27.5f);
            optionLabels[i].setFontScale(0.55f);
            optionLabels[i].setFontScaleY(0.01f);
            optionLabels[i].setWrap(true);
            innerOptionTable.add(optionLabels[i]).width(506f).height(55f).center();
            optionLabels[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Label thisLabel = (Label) event.getListenerActor();
                    if (!thisLabel.isTouchable())
                        return;

                    for (int i = 0; i < selectedOptionsLabels.length; i++) {
                        if (!selectedOptionsLabels[i].isVisible()) {
                            selectedOptionsLabels[i].setText(thisLabel.getText());
                            selectedOptionsLabels[i].setVisible(true);
                            thisLabel.setTouchable(Touchable.disabled);
                            thisLabel.getStyle().fontColor = Color.DARK_GRAY;
                            selectedOptionsButtons[i].setChecked(true);
                            selectedOptionsButtons[i].setTouchable(Touchable.enabled);

                            for(Button button : selectedOptionsButtons){
                                if(!button.isChecked())
                                    return;
                            }
                            acceptButton.setVisible(true);
                            return;
                        }
                    }
                }
            });
        }

        fillOptionsLabels();

        optionsPane = new ScrollPane(innerOptionTable);
        optionsPane.clearListeners();

        optionsTable.add(optionsLeft).padRight(25f);
        optionsTable.add(optionsPane).width(506f);
        optionsTable.add(optionsRight).padLeft(25f);
        mainTable.addActor(optionsTable);

        optionsLeft.setVisible(false);
        optionsLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsPane.setScrollX(optionsPane.getScrollX() - 506f);
                if (optionsPane.getScrollX() == 0f) {
                    optionsLeft.setVisible(false);
                }
                optionsRight.setVisible(true);
            }
        });

        optionsRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsPane.setScrollX(optionsPane.getScrollX() + 506f);
                if (optionsPane.getScrollX() == optionsPane.getMaxX()) {
                    optionsRight.setVisible(false);
                }
                optionsLeft.setVisible(true);
            }
        });

        Table selectedTable = new Table();
        selectedTable.setBounds(0f, 115f, mainStage.getWidth(), 335f);
        selectedTable.top().padRight(15f).defaults().padBottom(10f).padLeft(15f);

        Button.ButtonStyle selectedOptionStyle = new TextButton.TextButtonStyle();
        selectedOptionStyle.up = new TextureRegionDrawable(atlas.findRegion("button_option_empty"));
        selectedOptionStyle.down = new TextureRegionDrawable(atlas.findRegion("button_option_down"));
        selectedOptionStyle.checked = new TextureRegionDrawable(atlas.findRegion("button_option_filled"));

        Label.LabelStyle selectedOptionLabelStyle = new Label.LabelStyle(uiSkin.getFont("arial"), Color.BLACK);

        TextButton.TextButtonStyle timerStyle = new TextButton.TextButtonStyle();
        timerStyle.up = new TextureRegionDrawable(atlas.findRegion("timer"));
        timerStyle.font = uiSkin.getFont("default-font"); timerStyle.fontColor = Color.WHITE;

        selectedOptionsButtons = new Button[3];
        selectedOptionsLabels = new Label[3];

        for(int i=0; i< selectedOptionsButtons.length; i++){
            selectedOptionsButtons[i] = new Button(selectedOptionStyle);
            selectedOptionsLabels[i] = new Label("", selectedOptionLabelStyle);
            selectedOptionsLabels[i].setFontScale(0.65f); selectedOptionsLabels[i].setWrap(true);
            selectedOptionsButtons[i].setOrigin(selectedOptionsButtons[i].getPrefWidth() / 2,
                    selectedOptionsButtons[i].getPrefHeight() / 2);
            selectedOptionsButtons[i].setTransform(true);
            selectedOptionsButtons[i].setScaleX(0f);
            selectedOptionsButtons[i].add(selectedOptionsLabels[i])
                    .width(selectedOptionsButtons[i].getPrefWidth());
            selectedOptionsLabels[i].setAlignment(Align.center);
            selectedTable.add(selectedOptionsButtons[i]);

            selectedOptionsButtons[i].setTouchable(Touchable.disabled);
            selectedOptionsLabels[i].setVisible(false);
            final AtomicInteger aux = new AtomicInteger(i);
            selectedOptionsButtons[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for(Label option : optionLabels){
                        if(!option.isTouchable() && option.getText()
                                .equals(selectedOptionsLabels[aux.get()].getText())){
                            option.setTouchable(Touchable.enabled);
                            option.getStyle().fontColor = Color.WHITE;
                            event.getListenerActor().setTouchable(Touchable.disabled);
                            selectedOptionsLabels[aux.get()].setVisible(false);
                            selectedOptionsLabels[aux.get()].setText("");

                            acceptButton.setVisible(false);
                        }
                    }
                }
            });

            if(i==0)
                selectedTable.add().row();
        }
        mainTable.addActor(selectedTable);

        infoButton = new Button(
                new TextureRegionDrawable(atlas.findRegion("see_more_up")),
                new TextureRegionDrawable(atlas.findRegion("see_more_down")),
                new TextureRegionDrawable(atlas.findRegion("see_more_down"))
        );
        infoButton.setPosition(340f, 341f); mainStage.addActor(infoButton);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                infoButton.setChecked(true);
                if (fingerImage != null) {
                    tweenManager.killAll();
                    fingerImage.remove();
                }
                showInfo(currentExercise);
            }
        });


        TextButton timerButton = new TextButton("" +timerMaxTime, timerStyle);
        this.timer = timerButton.getLabel(); timerButton.getLabel().setFontScale(0.7f);
        timerButton.setPosition(428f, 341f); mainTable.addActor(timerButton);

        acceptButton = new Button(
                new TextureRegionDrawable(atlas.findRegion("continue_button_up")),
                new TextureRegionDrawable(atlas.findRegion("continue_button_down"))
        );
        acceptButton.setVisible(false);
        acceptButton.setPosition((mainStage.getWidth() - acceptButton.getPrefWidth()) / 2, 116f);
        acceptButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                acceptAnswer();
            }
        });

        mainTable.addActor(acceptButton);

        timelineTable = new Table();
        timelineTable.padLeft(17f).padRight(17f);
        timelineTable.setBackground(new TextureRegionDrawable(atlas.findRegion("timeline_panel")));
        timelineTable.setSize(timelineTable.getBackground().getMinWidth(),
                timelineTable.getBackground().getMinHeight());

        timelineLeft = new Button(
                new TextureRegionDrawable(atlas.findRegion("button_back_up")),
                new TextureRegionDrawable(atlas.findRegion("button_back_down"))
        );

        timelineRight = new Button(
                new TextureRegionDrawable(atlas.findRegion("button_forward_up")),
                new TextureRegionDrawable(atlas.findRegion("button_forward_down"))
        );

        timelineImages = new Image[exercises.length];
        Table innerTimelineTable = new Table();
        innerTimelineTable.padRight(8f).defaults().padLeft(8f);
        for(int i=0; i< exercises.length; i++){
            if(i == 0) {
                timelineImages[i] = new Image(timelineImagesAtlas.findRegion("" + (exercisePad + 1)));
                final AtomicInteger aux = new AtomicInteger(i);
                timelineImages[i].addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        showInfo(aux.get());
                    }
                });
            }
            else {
                timelineImages[i] = new Image(atlas.findRegion("unknown_image"));
            }
            innerTimelineTable.add(timelineImages[i]);
        }

        imagesPane = new ScrollPane(innerTimelineTable);
        imagesPane.clearListeners();

        imagesPane.layout();
        imagesPane.setScrollX(0f);
        timelineLeft.setVisible(false);
        timelineLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                imagesPane.setScrollX(imagesPane.getScrollX() - 485f);
                if (imagesPane.getScrollX() == 0f) {
                    timelineLeft.setVisible(false);
                }
                timelineRight.setVisible(true);
            }
        });

        timelineRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                imagesPane.setScrollX(imagesPane.getScrollX() + 485f);
                if (imagesPane.getScrollX() == imagesPane.getMaxX()) {
                    timelineRight.setVisible(false);
                }
                timelineLeft.setVisible(true);
            }
        });

        timelineTable.add(timelineLeft).padRight(25f);
        timelineTable.add(imagesPane).width(485f);
        timelineTable.add(timelineRight).padLeft(25f);

        topTable = new TopTable("KRONOS", exercises[currentExercise].name, true, new TopTable.BackButtonCallback() {
            @Override
            public void onClicked() {
                Assets.clickFX.play();
                game.loadNextScreen(Kronos.this, KonnectingGame.ScreenType.KRONOS_MENU);
            }
        });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        mainStage.addActor(topTable);


        infoTable = new Table();
        infoTable.padLeft(48f).padRight(48f).padTop(77f+mainStage.getPadBottom()).top();
        infoTable.setBounds(-mainStage.getPadLeft(), -mainStage.getPadBottom(),
                mainStage.getRealWidth(), mainStage.getRealHeight());
        Image infoTableBackground = new Image(atlas.findRegion("black_pixel"));
        infoTableBackground.setSize(mainStage.getRealWidth(), mainStage.getRealHeight());
        infoTableBackground.getColor().a = 0.9f; infoTable.addActor(infoTableBackground);

        infoImage = new Image(titleImagesAtlas.findRegion("" +(exercisePad+1)));
        infoTable.add(infoImage).padRight(20f)
                .size(infoImage.getPrefWidth() * 0.7f, infoImage.getPrefHeight() * 0.7f);

        infoTitle = new Label(exercises[currentExercise].name,
                new Label.LabelStyle(uiSkin.getFont("arial"), Color.WHITE));
        infoTitle.setFontScale(0.9f); infoTitle.setWrap(true);
        infoTable.add(infoTitle).width(258f).row();

        Table innerCommentTable = new Table(); innerCommentTable.top();
        infoComment = new Label(exercises[currentExercise].text,
                new Label.LabelStyle(uiSkin.getFont("arial"), Color.WHITE));
        infoComment.setFontScale(0.6f);
        infoComment.setWrap(true);
        innerCommentTable.add(infoComment).width(mainStage.getWidth() - 96f);

        ScrollPane infoPane = new ScrollPane(innerCommentTable);

        infoTable.add(infoPane).colspan(2).padTop(37f).height(540f);

        Button infoExitButton = new Button(
                new TextureRegionDrawable(atlas.findRegion("button_close_up")),
                new TextureRegionDrawable(atlas.findRegion("button_close_down"))
        );
        infoExitButton.setPosition(572f + mainStage.getPadLeft() * 2, 906f + mainStage.getPadBottom() * 2);
        infoExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                infoTable.setVisible(false);
                infoButton.setChecked(false);
                infoButton.setVisible(false);
                if (fingerImage != null) {
                    fingerImage = null;
                    openingNewQuestionAnimation2().start(tweenManager);
                }
            }
        });
        infoTable.addActor(infoExitButton);

        infoTable.setVisible(false); mainStage.addActor(infoTable);
        mainStage.addActor(timelineTable);

        openingNewQuestionAnimation().start(tweenManager);
    }

    private void openNewQuestion(){
        topTable.getCommentLabel().setText(exercises[currentExercise].name);
        mainImage.setDrawable(new TextureRegionDrawable(titleImagesAtlas.findRegion("" +
                (exercisePad + currentExercise + 1))));
        fillOptionsLabels();

        for(int i=0; i<selectedOptionsButtons.length; i++){
            selectedOptionsButtons[i].setChecked(false);
            selectedOptionsButtons[i].setTouchable(Touchable.disabled);
            selectedOptionsLabels[i].setVisible(false);
            selectedOptionsLabels[i].setText("");
        }

        timer.setText("" + timerMaxTime);
        openingNewQuestionAnimation().start(tweenManager);
    }

    private Timeline openingNewQuestionAnimation(){
        mainTable.setTouchable(Touchable.disabled);
        timelineTable.setTouchable(Touchable.disabled);
        infoButton.setVisible(true);

        float fingerUpTime = 0.5f, fingerMovementTime = 0.25f, fingerStoppedTime = 0.25f;

        fingerImage = new Image(atlas.findRegion("fingers3")){
            @Override
            public Actor hit(float x, float y, boolean touchable) {
                return null;
            }
        };

        float initialFingerPosition [] = {mainStage.getWidth()+mainStage.getPadLeft(),
                -fingerImage.getPrefHeight()-mainStage.getPadLeft()};
        fingerImage.setPosition(initialFingerPosition[0], initialFingerPosition[1]);
        float finalFingerPosition [] = {0f, -332f-mainStage.getPadBottom()};
        mainStage.addActor(fingerImage);

        return Timeline.createSequence()
                .push(Tween.mark().setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Timeline.createSequence()
                                .push(Tween.mark().setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        infoButton.setChecked(true);
                                    }
                                }))
                                .pushPause(0.5f)
                                .push(Tween.mark().setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        infoButton.setChecked(false);
                                    }
                                }))
                                .pushPause(0.25f)
                                .repeat(Tween.INFINITY, 0f)
                                .start(tweenManager);
                    }
                }))
                .push(Tween.to(fingerImage, ActorAccessor.MOVE, fingerMovementTime).ease(Linear.INOUT)
                        .target(finalFingerPosition[0], finalFingerPosition[1]))
                .pushPause(fingerUpTime)
                .push(Tween.to(fingerImage, ActorAccessor.MOVE, fingerMovementTime).ease(Linear.INOUT)
                        .target(initialFingerPosition[0], initialFingerPosition[1])
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                fingerImage.setDrawable(new TextureRegionDrawable(
                                        atlas.findRegion("fingers2")));
                            }
                        }))
                .pushPause(fingerStoppedTime)
                .push(Tween.to(fingerImage, ActorAccessor.MOVE, fingerMovementTime).ease(Linear.INOUT)
                        .target(finalFingerPosition[0], finalFingerPosition[1]))
                .pushPause(fingerUpTime)
                .push(Tween.to(fingerImage, ActorAccessor.MOVE, fingerMovementTime).ease(Linear.INOUT)
                        .target(initialFingerPosition[0], initialFingerPosition[1])
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                fingerImage.setDrawable(new TextureRegionDrawable(
                                        atlas.findRegion("fingers1")));
                            }
                        }))
                .pushPause(fingerStoppedTime)
                .push(Tween.to(fingerImage, ActorAccessor.MOVE, fingerMovementTime).ease(Linear.INOUT)
                        .target(finalFingerPosition[0], finalFingerPosition[1]))
                .pushPause(fingerUpTime)
                .push(Tween.to(fingerImage, ActorAccessor.MOVE, fingerMovementTime).ease(Linear.INOUT)
                        .target(initialFingerPosition[0], initialFingerPosition[1]))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        fingerImage.remove();
                        fingerImage = null;
                        infoButton.setVisible(false);
                        tweenManager.killAll();
                        openingNewQuestionAnimation2().start(tweenManager);
                    }
                });
    }

    private Timeline openingNewQuestionAnimation2(){
        float scaleTime = 0.5f;

        return Timeline.createParallel()
                .push(Tween.to(mainImage, ActorAccessor.SCALEY, scaleTime).target(1f))
                .push(Tween.to(optionLabels[0], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.55f))
                .push(Tween.to(optionLabels[1], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.55f))
                .push(Tween.to(optionLabels[2], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.55f))
                .push(Tween.to(optionLabels[3], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.55f))
                .push(Tween.to(optionLabels[4], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.55f))
                .push(Tween.to(optionLabels[5], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.55f))
                .push(Tween.to(selectedOptionsButtons[0], ActorAccessor.SCALEX, scaleTime).target(1f))
                .push(Tween.to(selectedOptionsButtons[1], ActorAccessor.SCALEX, scaleTime).target(1f))
                .push(Tween.to(selectedOptionsButtons[2], ActorAccessor.SCALEX, scaleTime).target(1f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        mainTable.setTouchable(Touchable.childrenOnly);

                        Tween.to(timer, LabelAccessor.NUMBER_CHANGE, timerMaxTime).target(0f).ease(Linear.INOUT)
                                .setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        acceptAnswer();
                                    }
                                }).start(tweenManager);
                    }
                });
    }

    private void acceptAnswer(){
        acceptButton.setVisible(false);
        mainTable.setTouchable(Touchable.disabled);
        tweenManager.killTarget(timer);

        float scaleTime = 0.5f;

        final Array<String> correctAnswer = new Array<>(exercises[currentExercise].answers);
        for(int i=0; i<correctAnswer.size; i++){
            int index = correctAnswer.indexOf(selectedOptionsLabels[i].getText().toString(), false);
            selectedOptionsLabels[i].setVisible(true);
            if(index != -1) {
                correctAnswer.swap(i, index);
                scoreCounter += 10;
            }
        }

        currentExercise++;
        final boolean finish;

        if(exerciseSize[kronosNumber] == currentExercise){
            StatsData statsData = SavedData.getStats();

            finish = true;

            //saving score if bigger
            switch (kronosNumber){
                case 0:
                    statsData.kronosScore1 = Math.max(statsData.kronosScore1, scoreCounter);
                    break;
                case 1:
                    statsData.kronosScore2 = Math.max(statsData.kronosScore2, scoreCounter);
                    break;
                case 2:
                    statsData.kronosScore3 = Math.max(statsData.kronosScore3, scoreCounter);
                    break;
                case 3:
                    statsData.kronosScore4 = Math.max(statsData.kronosScore4, scoreCounter);
                    break;
                case 4:
                    statsData.kronosScore5 = Math.max(statsData.kronosScore5, scoreCounter);
                    break;
                case 5:
                    statsData.kronosScore6 = Math.max(statsData.kronosScore6, scoreCounter);
                    break;
                case 6:
                    statsData.kronosScore7 = Math.max(statsData.kronosScore7, scoreCounter);
                    break;
            }

            //Saving game progress
            if(statsData.gameProgress < kronosNumber+2){
                statsData.gameProgress = kronosNumber+2;
            }

            SavedData.setStats(statsData);
        }
        else
            finish = false;

        Timeline checkCorrectAnswersTimeline = Timeline.createSequence();

        for(int i=0; i<selectedOptionsLabels.length; i++){
            final AtomicInteger aux = new AtomicInteger(i);

            TweenCallback callback;
            if(selectedOptionsLabels[i].getText().toString().equals(correctAnswer.get(i))) {
                callback = new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Assets.successFX.play();
                        topTable.getScoreLabel().setText("" +scoreCounter +"pt");
                    }
                };
            }
            else {
                callback = new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Assets.failFX.play();
                        selectedOptionsLabels[aux.get()].setText(correctAnswer.get(aux.get()));
                    }
                };
            }

            checkCorrectAnswersTimeline.push(Timeline.createSequence()
                    .push(Timeline.createSequence()
                            .push(Tween.mark().setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    selectedOptionsButtons[aux.get()].setChecked(false);
                                }
                            }))
                            .pushPause(0.125f)
                            .push(Tween.mark().setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    selectedOptionsButtons[aux.get()].setChecked(true);
                                }
                            }))
                            .pushPause(0.125f)
                            .setCallback(callback)
                            .repeat(4, 0f))
                    .pushPause(0.5f));
        }

        checkCorrectAnswersTimeline.beginParallel()
                .push(Tween.to(mainImage, ActorAccessor.SCALEY, scaleTime).target(0f))
                .push(Tween.to(optionLabels[0], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(optionLabels[1], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(optionLabels[2], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(optionLabels[3], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(optionLabels[4], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(optionLabels[5], LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(selectedOptionsButtons[0], ActorAccessor.SCALEX, scaleTime).target(0f))
                .push(Tween.to(selectedOptionsButtons[1], ActorAccessor.SCALEX, scaleTime).target(0f))
                .push(Tween.to(selectedOptionsButtons[2], ActorAccessor.SCALEX, scaleTime).target(0f))
                .end()
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (finish) {
                            tweenManager.killAll();

                            UpdateStatsDialog scoreDialog = new UpdateStatsDialog(Kronos.this, mainStage,
                                    new UpdateStatsDialog.ButtonType[]{
                                            UpdateStatsDialog.ButtonType.CONTINUE},
                                    new UpdateStatsDialog.ButtonType[]{
                                            UpdateStatsDialog.ButtonType.CONTINUE},
                                    new UpdateStatsDialog.ButtonType[]{
                                            UpdateStatsDialog.ButtonType.RETRY,
                                            UpdateStatsDialog.ButtonType.CONTINUE},
                                    new UpdateStatsDialog.UpdateStatsDialogCallback() {
                                        @Override
                                        public void onContinue() {
                                            game.loadNextScreen(Kronos.this, KonnectingGame.ScreenType.KRONOS_MENU);
                                        }

                                        @Override
                                        public void onGoBack() {
                                            //DO absolutely nothing :)
                                        }
                                    });
                            scoreDialog.updateStats();
                            return;
                        }

                        optionsLeft.setVisible(false);
                        optionsRight.setVisible(true);
                        optionsPane.setScrollX(0);


                        timelineTable.setTouchable(Touchable.childrenOnly);
                        imagesPane.setScrollX(currentExercise / 5 * 485f);
                        timelineLeft.setVisible(currentExercise / 5 != 0);
                        timelineRight.setVisible(currentExercise / 5 * 485f < imagesPane.getMaxX());

                        timelineImages[currentExercise].setDrawable(new TextureRegionDrawable(
                                timelineImagesAtlas.findRegion("" + (exercisePad + currentExercise + 1))));
                        final AtomicInteger aux = new AtomicInteger(currentExercise);
                        timelineImages[currentExercise].addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                infoTable.setVisible(false);
                                timelineImages[aux.get()].removeListener(this);
                                tweenManager.killAll();
                                timelineImages[aux.get()].getColor().a = 1f;
                                timelineImages[aux.get()].addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        showInfo(aux.get());
                                    }
                                });
                                openNewQuestion();
                            }
                        });

                        Timeline.createSequence()
                                .push(Tween.set(timelineImages[currentExercise], ActorAccessor.ALPHA)
                                        .target(0f))
                                .pushPause(0.25f)
                                .push(Tween.set(timelineImages[currentExercise], ActorAccessor.ALPHA)
                                        .target(1f))
                                .pushPause(0.25f)
                                .repeat(Tween.INFINITY, 0f).start(tweenManager);
                    }
                })
                .start(tweenManager);
    }

    private void fillOptionsLabels(){
        Array<String> answers = new Array<>();

        answers.add(exercises[currentExercise].answers[0]);
        answers.add(exercises[currentExercise].answers[1]);
        answers.add(exercises[currentExercise].answers[2]);

        for(int i=0; i<3; i++){
            String wrongAnswer;
            do {
                wrongAnswer = exercises[MathUtils.random(exercises.length-1)]
                        .answers[MathUtils.random(0,2)];
            }while (answers.contains(wrongAnswer, false));
            answers.add(wrongAnswer);
        }

        answers.shuffle();
        for (int i=0; i<optionLabels.length; i++) {
            optionLabels[i].getStyle().fontColor = Color.WHITE;
            optionLabels[i].setTouchable(Touchable.enabled);
            optionLabels[i].setText(answers.get(i));
        }
    }

    private void showInfo(int value){
        infoImage.setDrawable(new TextureRegionDrawable(
                titleImagesAtlas.findRegion("" + (exercisePad + value + 1))));
        infoTitle.setText(exercises[value].name);
        infoComment.setText(exercises[value].text);
        infoTable.setVisible(true);
    }

    public static class ExerciseInfoArray {
        public ExerciseInfo[] infos;

        public ExerciseInfoArray() {
        }
    }

    public static class ExerciseInfo {
        String  name;
        String [] answers;
        String text;

        public ExerciseInfo() {
        }
    }
}
