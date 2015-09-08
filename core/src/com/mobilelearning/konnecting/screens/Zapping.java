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
 * Created by AFFonseca on 30/08/2015.
 */
public class Zapping extends StandardScreen {
    private static final String titles [] = {
            "Escrita e alfabetos",
            "Suportes para registos escritos",
            "Escrever e enviar mensagens",
            "A Galáxia de Gutenberg e novos registos",
            "A Galáxia Marconi e a aldeia global"
    };

    private static final String comments [] = {
            "A escrita evoluiu através do tempos, mas não deixa de ser curioso a escrita " +
                    "hieroglífica ter surgido em diferentes continentes sem influência mútua e " +
                    "haver alfabetos que não pertencem a nenhum país mas que pretendem melhorar a " +
                    "comunicação. Verificou-se também a preocupação de, por um lado, proteger o " +
                    "código escrito por processos mecânicos ou digitais, bem como, de o enriquecer," +
                    " tentando dar-lhe a expressividade da língua falada.",
            "Os suportes para registos escritos tornaram-se mais resistentes, aumentaram a " +
                    "capacidade de armazenamento e a sua portabilidade.",
            "Antigamente eram poucos os que sabiam ler e escrever, essas capacidades estavam confinadas " +
                    "a uma elite. Na atualidade, o acesso à instrução é uma realidade. Com o " +
                    "aparecimento do email, da Web 2.0 e da comunicação síncrona online, falar e " +
                    "enviar mensagens está à distância de um clique. O imediato caracteriza as novas " +
                    "gerações, com acesso permanente à Internet e a dispositivos móveis.",
            "A imprensa de Gutenberg com caracteres móveis amplificou a escrita fonética, como " +
                    "refere  Jean Cloutier (1976). A reforçar a relevância desta invenção, Marshall " +
                    "McLuhan (1972) utiliza a expressão Galáxia de Gutenberg, a que se associam novos " +
                    "registos mecânicos que proporcionaram a expansão e divulgação do saber. É a " +
                    "força centrífuga da Galáxia de Gutenberg.\n\n" +
                    "Cloutier, J. ( 1979). A era de EMEREC. Lisboa: Instituo de Tecnologia Educativa.\n\n" +
                    "McLuhan, M. (1972). A Galáxia de Gutenberg: formação do homem tipográfico. " +
                    "São Paulo: Editora Nacional.",
            "A Galáxia Marconi, expressão de Marshall McLuhan (1974), surge com a invenção do t" +
                    "elégrafo elétrico. A partir daqui a diversidade de meios de comunicação tem " +
                    "contribuído para encurtar as distâncias e aproximar os Homens. É a força " +
                    "centrípeta da Galáxia Marconi e da aldeia global. Pessoas e ideias estão " +
                    "conectadas online (Siemens, 2005). Por outro lado, a facilidade em publicar " +
                    "online, contribuindo todos para a inteligência coletiva, como salienta Pierre " +
                    "Lévy (2000), exige um leitor crítico que saiba avaliar a credibilidade do que " +
                    "lê.\n\n" +
                    "Lévy, P. (2000). Cibercultura.  Lisboa: Piaget.\n" +
                    "McLuhan, M. (1974). Understanding Media. Editora Pensamento - Cultrix.\n" +
                    "Siemens, G. (2005). Connectivism: A learning theory for the digital age. " +
                    "International Journal of Instructional Technology & Distance Learning, 2."
    };

    private Kronos.ExerciseInfo exercises [];
    int[] exerciseSize = {13, 8, 10, 11, 19};

    private final int zappingNumber;
    private TextureAtlas atlas, imageAtlas;
    private final int timerMaxTime = 15;
    private int correctImageIndex, selectedImageIndex, currentExercise = 0, scoreCounter = 0;
    private Button infoButton, acceptButton;
    private Label textLabel, timer;
    private Image images [], imageHighlight, fingerImage = null;
    private TopTable topTable;
    private Table infoTable;

    public Zapping(KonnectingGame game, SpriteBatch batch, int zappingNumber) {
        super(game, batch);
        this.zappingNumber = zappingNumber -1;

        exercises = new Kronos.ExerciseInfo[exerciseSize[zappingNumber -1]];
        int exercisePad = Assets.numberOfImages;
        for(int i=0; i< zappingNumber -1; i++){
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
        TextureAtlas [] atlases = Assets.prepareZapping();
        atlas = atlases[0]; imageAtlas = atlases[1];
    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {
        super.show();

        backgroundStage.addActor(new Image(atlas.findRegion("background")));

        Table mainTable = new Table();
        mainTable.setSize(mainStage.getWidth(), mainStage.getHeight());
        mainTable.padTop(153f);
        mainStage.addActor(mainTable);

        infoButton = new Button(
                new TextureRegionDrawable(atlas.findRegion("see_more_up")),
                new TextureRegionDrawable(atlas.findRegion("see_more_down")),
                new TextureRegionDrawable(atlas.findRegion("see_more_down"))
        );
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                infoButton.setChecked(true);
                infoTable.setVisible(true);
                if (fingerImage != null) {
                    tweenManager.killAll();
                    fingerImage.remove();
                }
            }
        });
        mainTable.add(infoButton).padBottom(10f);

        Table textTable = new Table();
        textTable.setBackground(new TextureRegionDrawable(atlas.findRegion("text_background")));

        textLabel = new Label("",
                new Label.LabelStyle(uiSkin.getFont("default-font"), Color.BLACK));
        textLabel.setFontScale(0.55f);
        textLabel.setFontScaleY(0.01f); textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        textTable.add(textLabel).width(370f);
        mainTable.add(textTable).padBottom(10f)
                .size(textTable.getBackground().getMinWidth(), textTable.getBackground().getMinHeight());

        TextButton.TextButtonStyle timerStyle = new TextButton.TextButtonStyle();
        timerStyle.up = new TextureRegionDrawable(atlas.findRegion("timer"));
        timerStyle.font = uiSkin.getFont("default-font"); timerStyle.fontColor = Color.WHITE;

        TextButton timerButton = new TextButton("", timerStyle);
        timer = timerButton.getLabel(); timerButton.getLabel().setFontScale(0.7f);
        mainTable.add(timerButton).padBottom(10f).row();

        Table imageTable = new Table();
        imageTable.setBackground(new TextureRegionDrawable(atlas.findRegion("image_panel")));
        imageTable.setSize(imageTable.getBackground().getMinWidth(), imageTable.getBackground().getMinHeight());
        imageTable.padBottom(27f).defaults().pad(8f);

        images = new Image[4];
        for(int i=0; i<images.length; i++){
            images[i] = new Image(imageAtlas.findRegion("" +(i+1)));

            if(i<2){ //top images
                images[i].setOriginY(images[i].getPrefHeight());
            }
            if (i%2 == 1){ //right sided images
                images[i].setOriginX(images[i].getPrefWidth());
            }
            images[i].setScale(0f);

            final AtomicInteger imageNumber = new AtomicInteger(i);
            images[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageHighlight.setPosition(
                            event.getListenerActor().getX()-5f,
                            event.getListenerActor().getY()-5f);
                    selectedImageIndex = imageNumber.get();
                }
            });
            imageTable.add(images[i]);

            if(i%2 ==1)
                imageTable.row();
        }
        mainTable.add(imageTable).colspan(3).row();

        prepareNextExercise();

        acceptButton = new Button(
                new TextureRegionDrawable(atlas.findRegion("continue_button_up")),
                new TextureRegionDrawable(atlas.findRegion("continue_button_down"))
        );
        acceptButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                acceptAnswer();
            }
        });
        mainTable.add(acceptButton).colspan(3).padTop(-30f);

        //adding in the end because of layout
        mainTable.layout();
        imageHighlight = new Image(atlas.findRegion("image_mask"));
        imageHighlight.setPosition(images[0].getX() - 5f, images[0].getY() - 5f);
        imageHighlight.setVisible(false);
        imageTable.addActor(imageHighlight);


        //top table
        topTable = new TopTable("ZAPPING", titles[zappingNumber],
                true, new TopTable.BackButtonCallback() {
            @Override
            public void onClicked() {
                Assets.clickFX.play();
                game.loadNextScreen(Zapping.this, KonnectingGame.ScreenType.ZAPPING_MENU);
            }
        });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        mainStage.addActor(topTable);


        //info table
        infoTable = new Table();
        infoTable.padLeft(48f).padRight(48f).center();
        infoTable.setBounds(-mainStage.getPadLeft(), -mainStage.getPadBottom(),
                mainStage.getRealWidth(), mainStage.getRealHeight());
        Image infoTableBackground = new Image(atlas.findRegion("black_pixel"));
        infoTableBackground.setSize(mainStage.getRealWidth(), mainStage.getRealHeight());
        infoTableBackground.getColor().a = 0.9f; infoTable.addActor(infoTableBackground);

        Label infoTitle = new Label(titles[zappingNumber],
                new Label.LabelStyle(uiSkin.getFont("arial"), Color.WHITE));
        infoTitle.setWrap(true); infoTitle.setAlignment(Align.center);
        infoTable.add(infoTitle).width(mainStage.getWidth() - 96f).row();

        Table innerCommentTable = new Table(); innerCommentTable.top();
        Label infoComment = new Label(comments[zappingNumber],
                new Label.LabelStyle(uiSkin.getFont("arial"), Color.WHITE));
        infoComment.setFontScale(0.65f);
        infoComment.setWrap(true);
        innerCommentTable.add(infoComment).width(mainStage.getWidth() - 96f);

        ScrollPane infoPane = new ScrollPane(innerCommentTable);

        infoTable.add(infoPane).colspan(2).padTop(37f).height(580f);

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

        infoTable.setVisible(false);
        mainStage.addActor(infoTable);

        openingNewQuestionAnimation().start(tweenManager);

    }

    private Timeline openingNewQuestionAnimation(){
        setAllTouchable(Touchable.disabled);
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
                .push(Tween.to(textLabel, LabelAccessor.FONT_SCALE_Y, 0.25f).target(0.55f))
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
                .push(Tween.to(images[0], ActorAccessor.SCALEXY, scaleTime).target(1f))
                .push(Tween.to(images[1], ActorAccessor.SCALEXY, scaleTime).target(1f))
                .push(Tween.to(images[2], ActorAccessor.SCALEXY, scaleTime).target(1f))
                .push(Tween.to(images[3], ActorAccessor.SCALEXY, scaleTime).target(1f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        setAllTouchable(Touchable.enabled);
                        acceptButton.setVisible(true);
                        selectedImageIndex = 0;
                        imageHighlight.setPosition(images[0].getX() - 5f, images[0].getY() - 5f);
                        imageHighlight.setVisible(true);

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
        setAllTouchable(Touchable.disabled);
        tweenManager.killTarget(timer);

        float scaleTime = 0.5f;
        final boolean finish;

        if(selectedImageIndex == correctImageIndex){
            scoreCounter += 10;
        }

        currentExercise++;
        if(currentExercise == exerciseSize[zappingNumber]){
            finish = true;
            StatsData statsData = SavedData.getStats();

            //Saving score if bigger
            switch (zappingNumber){
                case 0:
                    statsData.zappingScore1 = Math.max(statsData.zappingScore1, scoreCounter);
                    break;
                case 1:
                    statsData.zappingScore2 = Math.max(statsData.zappingScore2, scoreCounter);
                    break;
                case 2:
                    statsData.zappingScore3 = Math.max(statsData.zappingScore3, scoreCounter);
                    break;
                case 3:
                    statsData.zappingScore4 = Math.max(statsData.zappingScore4, scoreCounter);
                    break;
                case 4:
                    statsData.zappingScore5 = Math.max(statsData.zappingScore5, scoreCounter);
                    break;
            }

            //Saving progress
            int isolatedValue = (statsData.gameProgress/(int)(Math.pow(10, (double) zappingNumber)))%10;
            if(isolatedValue == 0)
                statsData.gameProgress += (int)(Math.pow(10, (double) zappingNumber));

            SavedData.setStats(statsData);
        }
        else
            finish = false;

        Timeline checkCorrectAnswerTimeline = Timeline.createSequence();
        TweenCallback callback;
        if(selectedImageIndex == correctImageIndex) {
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
                    imageHighlight.setPosition(
                            images[correctImageIndex].getX() -5f,
                            images[correctImageIndex].getY() -5f);
                }
            };
        }

        checkCorrectAnswerTimeline.push(Timeline.createSequence()
                .push(Timeline.createSequence()
                        .push(Tween.mark().setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                imageHighlight.setVisible(false);
                            }
                        }))
                        .pushPause(0.125f)
                        .push(Tween.mark().setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                imageHighlight.setVisible(true);
                            }
                        }))
                        .pushPause(0.125f)
                        .setCallback(callback)
                        .repeat(4, 0f))
                .pushPause(0.5f))
                .push(Tween.mark().setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        imageHighlight.setVisible(false);
                    }
                }))
                .beginParallel()
                .push(Tween.to(textLabel, LabelAccessor.FONT_SCALE_Y, scaleTime).target(0.01f))
                .push(Tween.to(images[0], ActorAccessor.SCALEXY, scaleTime).target(0f))
                .push(Tween.to(images[1], ActorAccessor.SCALEXY, scaleTime).target(0f))
                .push(Tween.to(images[2], ActorAccessor.SCALEXY, scaleTime).target(0f))
                .push(Tween.to(images[3], ActorAccessor.SCALEXY, scaleTime).target(0f))
                .end()
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (finish) {
                            tweenManager.killAll();

                            UpdateStatsDialog scoreDialog = new UpdateStatsDialog(Zapping.this, mainStage,
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
                                            game.loadNextScreen(Zapping.this, KonnectingGame.ScreenType.ZAPPING_MENU);
                                        }

                                        @Override
                                        public void onGoBack() {
                                            //DO absolutely nothing :)
                                        }
                                    });
                            scoreDialog.updateStats();
                        } else {
                            prepareNextExercise();
                            openingNewQuestionAnimation().start(tweenManager);
                        }

                    }
                })
                .start(tweenManager);
    }

    private void setAllTouchable(Touchable it){
        for(Image image : images){
            image.setTouchable(it);
        }
        acceptButton.setTouchable(it);
    }

    private void prepareNextExercise(){
        timer.setText("" +timerMaxTime);
        textLabel.setText(exercises[currentExercise].name);

        Array<Integer> imageIndexes = new Array<>();

        //Getting a random answer from the available answer images;
        int correctImageNumber = Integer.parseInt(exercises[currentExercise]
                .answers[MathUtils.random(exercises[currentExercise].answers.length-1)]);
        imageIndexes.add(correctImageNumber);

        int randomImageIndex, randomExerciseIndex;
        for(int i=0; i< 3; i++) {
            do {
                do {
                    randomExerciseIndex = MathUtils.random(exercises.length - 1);
                }while (randomExerciseIndex == currentExercise);
                randomImageIndex = Integer.parseInt(exercises[randomExerciseIndex]
                        .answers[MathUtils.random(exercises[randomExerciseIndex].answers.length - 1)]);
            } while (imageIndexes.contains(randomImageIndex, false));
            imageIndexes.add(randomImageIndex);
        }

        imageIndexes.shuffle();
        correctImageIndex = imageIndexes.indexOf(correctImageNumber, false);

        for(int i=0; i<images.length; i++){
            images[i].setDrawable(new TextureRegionDrawable(imageAtlas.findRegion(""+imageIndexes.get(i))));
        }
    }
}
