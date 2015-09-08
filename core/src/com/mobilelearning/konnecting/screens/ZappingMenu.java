package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;
import com.mobilelearning.konnecting.SavedData;
import com.mobilelearning.konnecting.serviceHandling.json.StatsData;
import com.mobilelearning.konnecting.uiUtils.TopTable;
import com.mobilelearning.konnecting.uiUtils.UpdateStatsDialog;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AFFonseca on 30/08/2015.
 */
public class ZappingMenu extends StandardScreen {
    private static final String [] buttonTexts = {
            "Escrita e alfabetos",
            "Suporte para registos\nescritos",
            "Escrever e enviar\nmensagens",
            "A Galáxia de Gutenberg\ne novos registos: força\ncentrífuga",
            "A Galáxia Marconi e a\naldeia global: força\ncentrípeta",
    };

    private static final KonnectingGame.ScreenType zappingExercises [] = {
            KonnectingGame.ScreenType.ZAPPING1,
            KonnectingGame.ScreenType.ZAPPING2,
            KonnectingGame.ScreenType.ZAPPING3,
            KonnectingGame.ScreenType.ZAPPING4,
            KonnectingGame.ScreenType.ZAPPING5
    };

    private TextureAtlas atlas;

    public ZappingMenu(KonnectingGame game, SpriteBatch batch) {
        super(game, batch);
    }

    @Override
    public void load() {

    }

    @Override
    public void prepare() {
        super.prepare();
        atlas = Assets.prepareZappingMenu();
    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {
        super.show();

        int currentProgress = SavedData.getStats().gameProgress;
        boolean[] zappingProgress = new boolean[5];

        int numberOfCompleted = 0;
        for(int i=0; i< zappingProgress.length; i++){
            int isolatedValue = (currentProgress/(int)(Math.pow(10, (double)i)))%10;
            if (isolatedValue == 0)
                zappingProgress[i] = false;
            else {
                zappingProgress[i] = true;
                numberOfCompleted++;
            }
        }


        backgroundStage.addActor(new Image(atlas.findRegion("background")));


        StatsData statsData = SavedData.getStats();
        if(numberOfCompleted == 5 && currentProgress != 211111){
            SavedData.setGameProgress(211111);
            UpdateStatsDialog scoreDialog = new UpdateStatsDialog(ZappingMenu.this, mainStage,
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
                            game.loadNextScreen(ZappingMenu.this, KonnectingGame.ScreenType.ENDING_SCREEN);
                        }

                        @Override
                        public void onGoBack() {
                            //DO absolutely nothing :)
                        }
                    });
            scoreDialog.updateStats();
            return;
        }

        Table mainTable = new Table();
        mainTable.setSize(mainStage.getWidth(), mainStage.getHeight());
        mainTable.padTop(20f).defaults().padBottom(20f);
        mainStage.addActor(mainTable);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = uiSkin.getFont("default-font"); buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("button_up"));
        buttonStyle.down = new TextureRegionDrawable(atlas.findRegion("button_down"));

        for(int i=0; i<buttonTexts.length; i++){
            TextButton button = new TextButton(buttonTexts[i], buttonStyle);
            button.getLabel().setFontScale(0.5f);
            Image checkedImage = new Image(atlas.findRegion("icon_check"));
            checkedImage.setPosition(469f, 43f);
            checkedImage.setVisible(zappingProgress[i]);
            button.addActor(checkedImage);

            final AtomicInteger counter = new AtomicInteger(i);
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.loadNextScreen(ZappingMenu.this, zappingExercises[counter.get()]);
                }
            });

            mainTable.add(button).row();
        }

        TopTable topTable = new TopTable("ZAPPING TEMÁTICO", "Associe à expressão a imagem correspondente.",
                true, new TopTable.BackButtonCallback() {
            @Override
            public void onClicked() {
                Assets.clickFX.play();
                game.loadNextScreen(ZappingMenu.this, KonnectingGame.ScreenType.MAIN_MENU);
            }
        });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        mainStage.addActor(topTable);
        topTable.getScoreLabel().setText("" +
                (statsData.zappingScore1 + statsData.zappingScore2 + statsData.zappingScore3 +
                        statsData.zappingScore4 + statsData.zappingScore5) +"pt");
    }
}
