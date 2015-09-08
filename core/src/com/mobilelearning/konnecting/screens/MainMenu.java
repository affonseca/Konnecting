package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;
import com.mobilelearning.konnecting.SavedData;
import com.mobilelearning.konnecting.accessors.ActorAccessor;
import com.mobilelearning.konnecting.serviceHandling.handlers.GetStatsHandler;
import com.mobilelearning.konnecting.serviceHandling.handlers.JoinClassHandler;
import com.mobilelearning.konnecting.serviceHandling.handlers.LoginHandler;
import com.mobilelearning.konnecting.serviceHandling.handlers.OtherClassesHandler;
import com.mobilelearning.konnecting.serviceHandling.handlers.RegisterHandler;
import com.mobilelearning.konnecting.serviceHandling.handlers.UserClassesHandler;
import com.mobilelearning.konnecting.serviceHandling.json.*;
import com.mobilelearning.konnecting.serviceHandling.json.ClassInfo;
import com.mobilelearning.konnecting.serviceHandling.services.GetStatsService;
import com.mobilelearning.konnecting.serviceHandling.services.JoinClassService;
import com.mobilelearning.konnecting.serviceHandling.services.LoginService;
import com.mobilelearning.konnecting.serviceHandling.services.OtherClassesService;
import com.mobilelearning.konnecting.serviceHandling.services.RegisterService;
import com.mobilelearning.konnecting.serviceHandling.services.UserClassesService;
import com.mobilelearning.konnecting.uiUtils.TopTable;
import com.mobilelearning.konnecting.uiUtils.UpdateStatsDialog;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created with IntelliJ IDEA.
 * User: AFFonseca
 * Date: 12-01-2015
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
*/
public class MainMenu extends StandardScreen implements LoginHandler, RegisterHandler, OtherClassesHandler,
        JoinClassHandler, UserClassesHandler, GetStatsHandler {

    private TextureAtlas mainMenuAtlas;
    private TextButton.TextButtonStyle textButtonStyle;
    private Button.ButtonStyle continueButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;

    private Table loginTable;
    private TextField usernameText;
    private TextField passwordText;

    private TextButton zappingButton;

    private Table userClassesTable;
    private ClassArray userClasses;
    private TextButton userClassesJoinButton;
    private List<String> userClassesList;

    private Table otherClassesTable;
    private int joinedClassIndex;
    private ClassArray classesToJoin;
    private List<String> otherClassesList;

    private Table mainMenuTable;
    private Table leaderboardTable, leaderboardEntries, creditsTable;

    private static final String leaderboardTitles [] = {"TOTAL (KRONOS + ZAPPING)", "KRONOS",
            "ZAPPING", "KRONOS 1 - PRÉ-HISTÓRIA", "KRONOS 2 - HISTÓRIA", "KRONOS 3 - IDADE MÉDIA",
            "KRONOS 4 - XVII A XIX", "KRONOS 5 - 1920-1971", "KRONOS 6 - EMAILS & BROWSERS",
            "KRONOS 7 - WIFI & SELFIES", "ZAPPING 1 - ESCRITA E ALFABETOS",
            "ZAPPING 2 - SUPORTE PARA REGISTOS ESCRITOS", "ZAPPING 3 - ESCREVER E ENVIAR MENSAGENS",
            "ZAPPING 4 - A GALÁXIA DE GUTENBERG E NOVOS REGISTOS: FORÇA CENTRÍFUGA",
            "ZAPPING 5 - A GALÁXIA MARCONI E A ALDEIA GLOBAL: FORÇA CENTRÍPETA"};

    private int currentLeaderboardTypeID = 0;

    private Table optionsTable;

    private Dialog messageDialog;
    private Label messageLabel;

    private Table lastTable;
    private Table currentTable;


    public MainMenu(KonnectingGame game, SpriteBatch batch){
        super(game, batch);
    }

    @Override
    public void show() {
        super.show();

        backgroundStage.addActor(new Image(mainMenuAtlas.findRegion("background")));
        textButtonStyle = new TextButton.TextButtonStyle(
                new TextureRegionDrawable(mainMenuAtlas.findRegion("text_button_up")),
                new TextureRegionDrawable(mainMenuAtlas.findRegion("text_button_down")),
                null,
                uiSkin.getFont("default-font")
        );
        textButtonStyle.fontColor = textButtonStyle.downFontColor = Color.BLACK;

        TextButton.TextButtonStyle checkedButtonStyle = new TextButton.TextButtonStyle(
                new TextureRegionDrawable(mainMenuAtlas.findRegion("text_button_up")),
                new TextureRegionDrawable(mainMenuAtlas.findRegion("text_button_down")),
                new TextureRegionDrawable(mainMenuAtlas.findRegion("text_button_down")),
                uiSkin.getFont("default-font")
        );
        checkedButtonStyle.fontColor = textButtonStyle.downFontColor  = Color.BLACK;

        textFieldStyle = new TextField.TextFieldStyle(uiSkin.get(TextField.TextFieldStyle.class));
        textFieldStyle.font = uiSkin.getFont("default-font");
        textFieldStyle.messageFont = uiSkin.getFont("default-font");
        textFieldStyle.messageFontColor = textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = null;

        continueButtonStyle = new Button.ButtonStyle(
                new TextureRegionDrawable(mainMenuAtlas.findRegion("continue_button_up")),
                new TextureRegionDrawable(mainMenuAtlas.findRegion("continue_button_down")),
                null
        );

        createLoginTable();
        createUserClassesTable();
        createOtherClassesTable();
        createMainMenuTable();
        createLeaderboardTable();
        createCreditsTable();
        createOptionsTable();
        createDialogWindow();

        //Show no table!
        loginTable.setVisible(false);
        otherClassesTable.setVisible(false);
        userClassesTable.setVisible(false);
        mainMenuTable.setVisible(false);
        leaderboardTable.setVisible(false);
        creditsTable.setVisible(false);
        optionsTable.setVisible(false);

        if(SavedData.getCurrentClassID() == null){
            //login required
            loginTable.setVisible(true);
            currentTable = loginTable;

            if(SavedData.getUserID() != null){
                //login not required. Going to step 2
                startLoadingAnimation(false);

                UserClassesService getClasses = new UserClassesService(MainMenu.this);
                getClasses.requestUserClasses();
            }
        }
        //Class not required. Going to main menu!
        else{
            mainMenuTable.setVisible(true);
            currentTable = mainMenuTable;
        }


    }

    @Override
    public void load() {
    }

    @Override
    public void prepare() {
        super.prepare();
        mainMenuAtlas = Assets.prepareMainMenu();
        Assets.prepareGlobalAssets();
        uiSkin = Assets.uiSkin;

    }

    @Override
    public void unload() {
    }

    private void createLoginTable(){

        //Creating the login Table and it's components
        loginTable = new Table();
        loginTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight());

        Image background = new Image(new TextureRegionDrawable(mainMenuAtlas.findRegion("login_table")));
        background.setPosition((mainStage.getWidth() - background.getPrefWidth()) / 2, 410f);
        loginTable.addActor(background);
        loginTable.center().top().padTop(240f);

        TextButton loginButton = new TextButton("entrar", textButtonStyle);
        loginButton.getLabel().setFontScale(0.6f);
        loginButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                usernameText.getOnscreenKeyboard().show(false);
                startLoadingAnimation(false);

                LoginService login = new LoginService(MainMenu.this);
                login.requestLogin(usernameText.getText(), passwordText.getText());

            }
        }) ;

        TextButton registerButton = new TextButton("registar", textButtonStyle);
        registerButton.getLabel().setFontScale(0.6f);
        registerButton.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                Assets.clickFX.play();
                usernameText.getOnscreenKeyboard().show(false);
                startLoadingAnimation(false);

                RegisterService register = new RegisterService(MainMenu.this);
                register.requestRegistration(usernameText.getText(), passwordText.getText());

            }
        }) ;

        usernameText = new TextField("", textFieldStyle);

        passwordText = new TextField("", textFieldStyle);
        passwordText.setPasswordCharacter('*');
        passwordText.setPasswordMode(true);

        loginTable.add(usernameText).size(350f, 60f).padBottom(94f).row();
        loginTable.add(passwordText).size(350f, 60f).padBottom(142f).row();
        loginTable.add(loginButton).padBottom(42f).row();
        loginTable.add(registerButton).row();
        //loginTable.debug();

        mainStage.addActor(loginTable);

    }

    private void createUserClassesTable(){
        //Now creating the User Classes Table
        userClassesTable = new Table();
        userClassesTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight() + mainStage.getPadBottom());

        Image background = new Image(new TextureRegionDrawable(mainMenuAtlas.findRegion("user_classes_table")));
        background.setPosition((mainStage.getWidth() - background.getPrefWidth()) / 2 + 2f, 290f);
        userClassesTable.addActor(background);
        userClassesTable.center().top().padTop(259f + mainStage.getPadBottom());

        userClassesList = new List<>(uiSkin);
        userClassesList.getStyle().font = uiSkin.getFont("default-font");
        userClassesList.getStyle().selection = new TextureRegionDrawable(mainMenuAtlas.findRegion("list_color"));
        ScrollPane classesPane = new ScrollPane(userClassesList,
                new ScrollPane.ScrollPaneStyle(textFieldStyle.background, null, null, null, null));
        classesPane.setScrollBarPositions(false, true);
        classesPane.setOverscroll(false, true);

        Button continueButton2 = new Button(continueButtonStyle);
        continueButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                if (userClasses.classes.size == 0)
                    return;

                startLoadingAnimation(false);

                ClassInfo chosenClass = userClasses.classes.get(userClassesList.getSelectedIndex());
                GetStatsService getScoreService = new GetStatsService(MainMenu.this);
                getScoreService.requestStats(chosenClass.classID);

            }
        }) ;


        userClassesJoinButton = new TextButton("entrar numa\nturma", textButtonStyle);
        userClassesJoinButton.getLabel().setFontScale(0.6f);
        userClassesJoinButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                startLoadingAnimation(false);

                OtherClassesService getClasses = new OtherClassesService(MainMenu.this);
                getClasses.requestOtherClasses();

            }
        }) ;

        userClassesTable.add(classesPane).size(430f, 331f).row();
        userClassesTable.add(userClassesJoinButton).padTop(100f).row();
        userClassesTable.add(continueButton2).padTop(25f).row();

        TopTable topTable = new TopTable("ESCOLHER TURMA",
                "Escolha uma turma para jogar. Se ainda não tem nenhuma deve inscrever-se.", false,
                new TopTable.BackButtonCallback() {
                    @Override
                    public void onClicked() {
                        if (lastTable == loginTable || lastTable == otherClassesTable) {
                            changeTable(loginTable, false);

                            SavedData.clearUserData();
                            usernameText.setText("");
                            passwordText.setText("");

                        } else
                            changeTable(optionsTable, false);
                    }
                });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        userClassesTable.addActor(topTable);

        mainStage.addActor(userClassesTable);

    }

    private void createOtherClassesTable(){
        //Now creating the other Classes Table

        otherClassesTable = new Table();
        otherClassesTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight() + mainStage.getPadBottom());

        Image background = new Image(new TextureRegionDrawable(mainMenuAtlas.findRegion("other_classes_table")));
        background.setPosition((mainStage.getWidth() - background.getPrefWidth()) / 2 + 2f, 290f);
        otherClassesTable.addActor(background);
        otherClassesTable.center().top().padTop(259f + mainStage.getPadBottom());

        otherClassesList = new List<>(uiSkin);
        otherClassesList.getStyle().font = uiSkin.getFont("default-font");
        userClassesList.getStyle().selection = new TextureRegionDrawable(mainMenuAtlas.findRegion("list_color"));
        ScrollPane classesPane = new ScrollPane(otherClassesList,
                new ScrollPane.ScrollPaneStyle(textFieldStyle.background, null, null, null, null));
        classesPane.setScrollBarPositions(false, true);
        classesPane.setOverscroll(false, true);

        Button joinButton = new Button(continueButtonStyle);
        joinButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                startLoadingAnimation(false);

                joinedClassIndex = otherClassesList.getSelectedIndex();
                long classToJoinID = classesToJoin.classes.get(joinedClassIndex).classID;

                JoinClassService join = new JoinClassService(MainMenu.this);
                join.requestJoinClass("" + classToJoinID);

            }
        }) ;

        otherClassesTable.add(classesPane).size(430, 331f).row();
        otherClassesTable.add(joinButton).padTop(100f).row();

        TopTable topTable = new TopTable("INSCREVER",
                "Escolha uma turma para se inscrever. Terá de esperar pela confirmação do professor.", false,
                new TopTable.BackButtonCallback() {
                    @Override
                    public void onClicked() {
                        if (lastTable == userClassesTable) {

                            startLoadingAnimation(false);

                            UserClassesService myClassesService = new UserClassesService(MainMenu.this);
                            myClassesService.requestUserClasses();

                        } else {
                            changeTable(optionsTable, false);
                        }
                    }
                });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        otherClassesTable.addActor(topTable);

        mainStage.addActor(otherClassesTable);

    }


    private void createMainMenuTable(){
        mainMenuTable = new Table();
        mainMenuTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight());
        mainMenuTable.bottom().defaults().padBottom(20f);

        Image alienImage = new Image(mainMenuAtlas.findRegion("alien"));
        alienImage.setPosition(250f, -318f);

        TextButton kronosButton = new TextButton("kronos", textButtonStyle);
        kronosButton.getLabel().setFontScale(0.6f);
        kronosButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                int progress = SavedData.getStats().gameProgress;
                if (progress == 0)
                    game.loadNextScreen(MainMenu.this, KonnectingGame.ScreenType.WELCOME_SCREEN);
                else
                    game.loadNextScreen(MainMenu.this, KonnectingGame.ScreenType.KRONOS_MENU);
            }
        }) ;

        zappingButton = new TextButton("zapping", textButtonStyle);
        zappingButton.getLabel().setFontScale(0.6f);
        zappingButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                game.loadNextScreen(MainMenu.this, KonnectingGame.ScreenType.ZAPPING_MENU);
            }
        });

        int progress = SavedData.getStats().gameProgress;
        if(progress < 1000){
            zappingButton.setTouchable(Touchable.disabled);
            zappingButton.getColor().a = 0.5f;
        }

        TextButton leaderboardButton = new TextButton("leaderboard", textButtonStyle);
        leaderboardButton.getLabel().setFontScale(0.6f);
        leaderboardButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();

                mainMenuTable.setTouchable(Touchable.disabled);
                UpdateStatsDialog scoreDialog = new UpdateStatsDialog(MainMenu.this, mainStage,
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.RETRY,
                                UpdateStatsDialog.ButtonType.GO_BACK
                        },
                        new UpdateStatsDialog.GetLeaderboardScoresDialogCallback() {
                            @Override
                            public void onSuccess(LeaderboardScoresData response) {
                                fillLeaderboardsTable(response);
                                changeTable(leaderboardTable, true);
                            }

                            @Override
                            public void onContinue() {
                                //Continue is not an option xD
                            }

                            @Override
                            public void onGoBack() {
                                mainMenuTable.setTouchable(Touchable.childrenOnly);
                            }
                        });
                scoreDialog.getLeaderboardScores(currentLeaderboardTypeID);
            }
        });

        TextButton creditsButton = new TextButton("créditos", textButtonStyle);
        creditsButton.getLabel().setFontScale(0.6f);
        creditsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                changeTable(creditsTable, true);
            }
        }) ;

        TextButton optionsButton = new TextButton("opções", textButtonStyle);
        optionsButton.getLabel().setFontScale(0.6f);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                changeTable(optionsTable, true);
            }
        }) ;


        TextButton logoutButton = new TextButton("logout", textButtonStyle);
        logoutButton.getLabel().setFontScale(0.6f);
        logoutButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                UpdateStatsDialog scoreDialog = new UpdateStatsDialog(MainMenu.this, mainStage,
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.CONTINUE,
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.CONTINUE,
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.CONTINUE,
                                UpdateStatsDialog.ButtonType.RETRY,
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.UpdateStatsDialogCallback() {
                            @Override
                            public void onContinue() {
                                changeTable(loginTable, false);

                                SavedData.clearUserData();
                                usernameText.setText("");
                                passwordText.setText("");
                            }

                            @Override
                            public void onGoBack() {
                                //DO absolutely nothing :)
                            }
                        });
                scoreDialog.updateStats();
            }
        }) ;

        Label.LabelStyle versionStyle = new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE);
        versionStyle.background = new TextureRegionDrawable(new TextureRegion(Assets.loading.getKeyFrame(0),
                        0, Assets.loading.getKeyFrame(0).getRegionHeight()/10, 1, 1));
        Label version = new Label("Versão: " +Assets.currentVersion, versionStyle);
        version.setAlignment(Align.center); version.setFontScale(0.45f);

        mainMenuTable.addActor(alienImage);
        mainMenuTable.add(kronosButton).row();
        mainMenuTable.add(zappingButton).row();
        mainMenuTable.add(leaderboardButton).row();
        mainMenuTable.add(creditsButton).row();
        mainMenuTable.add(optionsButton).row();
        mainMenuTable.add(logoutButton).row();
        mainMenuTable.add(version).padTop(155f).padBottom(15f);

        mainStage.addActor(mainMenuTable);

    }

    private void createLeaderboardTable(){
        leaderboardTable = new Table();
        leaderboardTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight() + mainStage.getPadBottom());
        leaderboardTable.top().padTop(130f + mainStage.getPadBottom());

        Table innerLeaderboardTable = new Table();
        innerLeaderboardTable.setBackground(new TextureRegionDrawable(mainMenuAtlas.findRegion("leaderboard_title")));
        innerLeaderboardTable.padLeft(29f).padRight(29f);

        Button backButton = new Button(
                new TextureRegionDrawable(mainMenuAtlas.findRegion("button_back_up")),
                new TextureRegionDrawable(mainMenuAtlas.findRegion("button_back_down")));
        innerLeaderboardTable.add(backButton).padRight(19f);

        final Label leaderboradTitleLabel = new Label(leaderboardTitles[currentLeaderboardTypeID],
                new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE));
        leaderboradTitleLabel.setFontScale(0.4f); leaderboradTitleLabel.setAlignment(Align.center);
        leaderboradTitleLabel.setWrap(true); innerLeaderboardTable.add(leaderboradTitleLabel).width(418f);

        Button forwardButton = new Button(
                new TextureRegionDrawable(mainMenuAtlas.findRegion("button_forward_up")),
                new TextureRegionDrawable(mainMenuAtlas.findRegion("button_forward_down")));
        innerLeaderboardTable.add(forwardButton).padLeft(19f);

        leaderboardTable.add(innerLeaderboardTable).size(
                innerLeaderboardTable.getBackground().getMinWidth(),
                innerLeaderboardTable.getBackground().getMinHeight()).row();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int aux = currentLeaderboardTypeID - 1;
                final int temporaryLeaderboardID;
                if (aux < 0)
                    aux = leaderboardTitles.length - 1;
                temporaryLeaderboardID = aux;

                leaderboardTable.setTouchable(Touchable.disabled);
                UpdateStatsDialog scoreDialog = new UpdateStatsDialog(MainMenu.this, mainStage,
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.RETRY,
                                UpdateStatsDialog.ButtonType.GO_BACK
                        },
                        new UpdateStatsDialog.GetLeaderboardScoresDialogCallback() {
                            @Override
                            public void onSuccess(LeaderboardScoresData response) {
                                currentLeaderboardTypeID = temporaryLeaderboardID;
                                leaderboradTitleLabel.setText(leaderboardTitles[currentLeaderboardTypeID]);
                                fillLeaderboardsTable(response);
                                leaderboardTable.setTouchable(Touchable.childrenOnly);
                            }

                            @Override
                            public void onContinue() {
                                //Continue is not an option xD
                            }

                            @Override
                            public void onGoBack() {
                                leaderboardTable.setTouchable(Touchable.childrenOnly);
                            }
                        });
                scoreDialog.getLeaderboardScores(temporaryLeaderboardID);
            }
        });

        forwardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int aux = currentLeaderboardTypeID + 1;
                final int temporaryLeaderboardID;
                if (aux >= leaderboardTitles.length)
                    aux = 0;
                temporaryLeaderboardID = aux;

                leaderboardTable.setTouchable(Touchable.disabled);
                UpdateStatsDialog scoreDialog = new UpdateStatsDialog(MainMenu.this, mainStage,
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.GO_BACK},
                        new UpdateStatsDialog.ButtonType[]{
                                UpdateStatsDialog.ButtonType.RETRY,
                                UpdateStatsDialog.ButtonType.GO_BACK
                        },
                        new UpdateStatsDialog.GetLeaderboardScoresDialogCallback() {
                            @Override
                            public void onSuccess(LeaderboardScoresData response) {
                                currentLeaderboardTypeID = temporaryLeaderboardID;
                                leaderboradTitleLabel.setText(leaderboardTitles[currentLeaderboardTypeID]);
                                fillLeaderboardsTable(response);
                                leaderboardTable.setTouchable(Touchable.childrenOnly);
                            }

                            @Override
                            public void onContinue() {
                                //Continue is not an option xD
                            }

                            @Override
                            public void onGoBack() {
                                leaderboardTable.setTouchable(Touchable.childrenOnly);
                            }
                        });
                scoreDialog.getLeaderboardScores(temporaryLeaderboardID);
            }
        });

        leaderboardEntries = new Table();
        leaderboardEntries.center().top().defaults().padBottom(-10f);
        leaderboardTable.add(leaderboardEntries).width(mainStage.getWidth()-40f);

        //Adding the top bar
        TopTable topTable = new TopTable("LEADERBOARD",
                "Lista dos 5 melhores jogadores da turma, assim como da tua posição na turma.", false,
                new TopTable.BackButtonCallback() {
                    @Override
                    public void onClicked() {
                        changeTable(mainMenuTable, false);
                    }
                });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        leaderboardTable.addActor(topTable);

        mainStage.addActor(leaderboardTable);

    }

    private void createCreditsTable(){
        creditsTable = new Table();
        creditsTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight()+mainStage.getPadBottom());

        creditsTable.setBackground(new TextureRegionDrawable(mainMenuAtlas.findRegion("credits_background")));

        creditsTable.addActor(new Image(mainMenuAtlas.findRegion("credits")));

        //Adding the top bar
        TopTable topTable = new TopTable("CRÉDITOS", "" +
                "Informação de todos os responsáveis pela criação deste projeto.", false,
                new TopTable.BackButtonCallback() {
                    @Override
                    public void onClicked() {
                        changeTable(mainMenuTable, false);
                    }
                });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        creditsTable.addActor(topTable);

        mainStage.addActor(creditsTable);

    }

    private void createOptionsTable(){
        optionsTable = new Table();
        optionsTable.setBounds(0, 0, mainStage.getWidth(), mainStage.getHeight() + mainStage.getPadBottom());
        optionsTable.center().defaults().padBottom(60f);


        TextButton changeClass = new TextButton("Mudar\nde Turma", textButtonStyle);
        changeClass.getLabel().setFontScale(0.6f);
        changeClass.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                startLoadingAnimation(false);

                UserClassesService getClasses = new UserClassesService(MainMenu.this);
                getClasses.requestUserClasses();

            }

        }) ;

        TextButton joinClass = new TextButton("Inscrever\nnuma turma", textButtonStyle);
        joinClass.getLabel().setFontScale(0.6f);
        joinClass.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Assets.clickFX.play();
                startLoadingAnimation(false);

                OtherClassesService getClasses = new OtherClassesService(MainMenu.this);
                getClasses.requestOtherClasses();

            }
        }) ;

        TextButton resetData = new TextButton("Recomeçar\no jogo", textButtonStyle);
        resetData.getLabel().setFontScale(0.6f);
        resetData.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                StatsData statsData = SavedData.getStats();
                statsData.gameProgress = 0;
                SavedData.setStats(statsData);
                zappingButton.setTouchable(Touchable.disabled);
                zappingButton.getColor().a = 0.5f;
            }
        }) ;

        optionsTable.add(changeClass).center();  optionsTable.row();
        optionsTable.add(joinClass).center();  optionsTable.row();
        optionsTable.add(resetData).center();  optionsTable.row();

        TopTable topTable = new TopTable("OPÇÕES", "", false,
                new TopTable.BackButtonCallback() {
                    @Override
                    public void onClicked() {
                        changeTable(mainMenuTable, false);
                    }
                });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        optionsTable.addActor(topTable);

        mainStage.addActor(optionsTable);
    }

    private void createDialogWindow(){
        messageDialog = new Dialog("", new Window.WindowStyle(
                uiSkin.getFont("default-font"),
                Color.WHITE,
                new TextureRegionDrawable(Assets.miscellaneous.findRegion("dialog_panel"))
        ));
        messageDialog.getCell(messageDialog.getContentTable()).expandY();
        messageDialog.getContentTable().center().padTop(12f);

        //messageDialog.getCell(messageDialog.getButtonTable()).height(303f);
        messageDialog.getButtonTable().center().padBottom(34f).defaults().padTop(11f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE);

        messageLabel = new Label("", labelStyle);
        messageLabel.setFontScale(0.6f);
        messageLabel.setAlignment(Align.center); messageDialog.text(messageLabel);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(
                new TextureRegionDrawable(Assets.miscellaneous.findRegion("dialog_button_up")),
                new TextureRegionDrawable(Assets.miscellaneous.findRegion("dialog_button_down")),
                null, uiSkin.getFont("default-font")
        );
        buttonStyle.fontColor = Color.WHITE;
        TextButton okButton = new TextButton("OK", buttonStyle);
        okButton.getLabel().setFontScale(0.8f);
        okButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                currentTable.setTouchable(Touchable.enabled);
            }
        }) ;
        messageDialog.button(okButton);

    }

    private void showMessage(String message){
        //Since dialog is buggy and I cannot make it \n if the message is bigger than the mainStage:
        currentTable.setTouchable(Touchable.disabled);

        String newMessage = "";

        while (true){
            int currentCharacter = 26;

            if(message.length() <= currentCharacter){
                newMessage = newMessage + message;
                break;
            }

            while(message.charAt(currentCharacter-1) != ' ' && currentCharacter >0)
                currentCharacter--;

            if(currentCharacter == 0)
                currentCharacter = 26;

            newMessage = newMessage + message.substring(0, currentCharacter) + "\n";
            message = message.substring(currentCharacter);

        }
        messageLabel.setText(newMessage);
        messageDialog.show(mainStage);
    }

    private void changeTable(Table to, boolean goFront){
        to.setTouchable(Touchable.disabled);
        currentTable.setTouchable(Touchable.disabled);

        float newTableStart = mainStage.getWidth(), oldTableEnd = - mainStage.getWidth();

        if(!goFront){
            newTableStart *= -1;
            oldTableEnd *= -1;
        }

        to.setX(newTableStart);
        to.setVisible(true);
        Tween.to(to, ActorAccessor.MOVE_X, 1.0f).target(0.0f).start(tweenManager);
        Tween.to(currentTable, ActorAccessor.MOVE_X, 1.0f).target(oldTableEnd)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if(type != TweenCallback.COMPLETE)
                            return;
                        lastTable.setVisible(false);
                        currentTable.setTouchable(Touchable.enabled);
                        lastTable.setTouchable(Touchable.enabled);
                    }
                })
                .start(tweenManager);
        lastTable = currentTable;
        currentTable = to;
    }

    @Override
    public void onLoginSuccess(UserData response) {
        //Login was successful. Now it's necessary to load the classes.
        SavedData.setUserData(response);

        UserClassesService getClasses = new UserClassesService(MainMenu.this);
        getClasses.requestUserClasses();

    }

    @Override
    public void onLoginError(String error) {
        stopLoadingAnimation();
        showMessage(error);
    }

    @Override
    public void onRegistrationSuccess(UserData response) {
        onLoginSuccess(response);
    }

    @Override
    public void onRegistrationError(String error) {
        stopLoadingAnimation();
        showMessage(error);
    }

    @Override
    public void onGettingUserClassesSuccess(ClassArray response) {

        stopLoadingAnimation();
        if(currentTable == optionsTable)
            userClassesJoinButton.setVisible(false);
        else
            userClassesJoinButton.setVisible(true);
        if(currentTable == otherClassesTable)
            changeTable(userClassesTable, false);
        else
            changeTable(userClassesTable, true);

        Array<String> classesArray = new Array<>(response.classes.size);
        for(ClassInfo it:response.classes){
            classesArray.add(it.className);
        }

        userClasses = response;
        userClassesList.setItems(classesArray);
    }

    @Override
    public void onGettingUserClassesError(String error) {
        stopLoadingAnimation();
        showMessage(error);
    }

    @Override
    public void onGettingOtherClassesSuccess(ClassArray response) {
        stopLoadingAnimation();

        changeTable(otherClassesTable, true);

        Array<String> classesArray = new Array<>(response.classes.size);
        for(ClassInfo it:response.classes){
            classesArray.add(it.className);
        }

        classesToJoin = response;
        otherClassesList.setItems(classesArray);
    }

    @Override
    public void onGettingOtherClassesError(String error) {
        stopLoadingAnimation();
        showMessage(error);
    }

    @Override
    public void onJoinClassSuccess() {
        stopLoadingAnimation();
        showMessage("Pedido de entrada adicionado com sucesso!");

        otherClassesList.getItems().removeIndex(joinedClassIndex);
        classesToJoin.classes.removeIndex(joinedClassIndex);
    }

    @Override
    public void onJoinClassError(String error) {
        stopLoadingAnimation();
        showMessage(error);
    }

    @Override
    public void onGetStatsSuccess(StatsData response) {
        stopLoadingAnimation();
        SavedData.setStats(response);

        if(response.gameProgress >= 1000) {
            zappingButton.setTouchable(Touchable.enabled);
            zappingButton.getColor().a = 1f;
        }

        ClassInfo chosenClass = userClasses.classes.get(userClassesList.getSelectedIndex());
        SavedData.setCurrentClass(chosenClass);

        if(lastTable == loginTable || lastTable == otherClassesTable)
            changeTable(mainMenuTable, true);
        else
            changeTable(optionsTable, false);
    }

    @Override
    public void onGetStatsError(String error) {
        stopLoadingAnimation();
        showMessage(error);
    }

    private void fillLeaderboardsTable(LeaderboardScoresData scoresData){
        leaderboardEntries.clear();

        Label.LabelStyle entryStyle = new Label.LabelStyle(uiSkin.getFont("default-font"), Color.WHITE);

        int counter = scoresData.leaderboardScores.size;
        if(counter == 5)
            counter++; //if list complete, add the possibility of outside list

        for(int i=0; i<counter; i++){
            String position, name, score;
            TextureRegionDrawable positionBackground;

            if(i==5){
                if(scoresData.leaderboardPosition.position < 6)
                    continue;

                position = "" +scoresData.leaderboardPosition.position;
                name = SavedData.getUsername();
                score = "" +scoresData.leaderboardPosition.leaderboardScore +" pt";
                positionBackground = new TextureRegionDrawable(
                        mainMenuAtlas.findRegion("leaderboard_personal"));
            }
            else {
                position = "" +(i+1);
                name = scoresData.leaderboardScores.get(i).username;
                score = "" +scoresData.leaderboardScores.get(i).score +" pt";
                positionBackground = new TextureRegionDrawable(
                        mainMenuAtlas.findRegion("leaderboard_top"));
            }

            Container<Label> positionContainer = new Container<>();
            positionContainer.setBackground(positionBackground);
            Label positionLabel = new Label(position, entryStyle);
            positionLabel.setAlignment(Align.right); positionLabel.setFontScale(0.8f);
            positionContainer.setActor(positionLabel);
            leaderboardEntries.add(positionContainer).padRight(20f)
                    .size(positionBackground.getMinWidth(), positionBackground.getMinHeight());

            Label nameLabel = new Label(name, entryStyle);
            leaderboardEntries.add(nameLabel).left().expandX();

            float shrink = 0f;
            if(name.length() > 10){
                shrink = (name.length()-10)/10f;
            }
            shrink = shrink/2;
            nameLabel.setFontScale(0.6f - 0.5f * shrink);

            Label scoreLabel = new Label(score, entryStyle);
            scoreLabel.setAlignment(Align.right); scoreLabel.setFontScale(0.6f);
            leaderboardEntries.add(scoreLabel).right().padRight(10f).row();

            if(i==5){
                leaderboardEntries.getCell(positionContainer).padTop(10f);
                leaderboardEntries.getCell(nameLabel).padTop(10f);
                leaderboardEntries.getCell(scoreLabel).padTop(10f);
            }
        }

    }
}
