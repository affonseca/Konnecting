package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;
import com.mobilelearning.konnecting.SavedData;
import com.mobilelearning.konnecting.serviceHandling.json.StatsData;
import com.mobilelearning.konnecting.uiUtils.TopTable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AFFonseca on 02/09/2015.
 */
public class KronosMenu extends StandardScreen {
    private static final float imagesPositions [][] = {
            {41f, 658f}, {240f, 658f}, {438.5f, 658f},
            {340f, 409f}, {142f, 409f},
            {41f, 165f}, {240f, 165f}, {438.5f, 165f},
    };

    private static final KonnectingGame.ScreenType screens [] = {
            KonnectingGame.ScreenType.KRONOS1,
            KonnectingGame.ScreenType.KRONOS2,
            KonnectingGame.ScreenType.KRONOS3,
            KonnectingGame.ScreenType.KRONOS4,
            KonnectingGame.ScreenType.KRONOS5,
            KonnectingGame.ScreenType.KRONOS6,
            KonnectingGame.ScreenType.KRONOS7,
            KonnectingGame.ScreenType.ZAPPING_MENU,
    };

    TextureAtlas atlas;

    public KronosMenu(KonnectingGame game, SpriteBatch batch) {
        super(game, batch);
    }

    @Override
    public void load() {

    }

    @Override
    public void prepare() {
        super.prepare();
        atlas = Assets.prepareKronosMenu();
    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {
        backgroundStage.addActor(new Image(atlas.findRegion("background")));

        Table imagesTable = new Table();
        imagesTable.setBackground(new TextureRegionDrawable(atlas.findRegion("levels")));
        imagesTable.setSize(
                imagesTable.getBackground().getMinWidth(),
                imagesTable.getBackground().getMinHeight()
        );
        mainStage.addActor(imagesTable);

        StatsData statsData = SavedData.getStats();
        for(int i=0; i< imagesPositions.length; i++){
            Image image;
            if(statsData.gameProgress-1 >= i){
                image = new Image(atlas.findRegion("current"));
                image.setPosition(imagesPositions[i][0] - 4f, imagesPositions[i][1] - 4f);

                final AtomicInteger aux = new AtomicInteger(i);
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Assets.clickFX.play();
                        if(aux.get() == 7 && SavedData.getStats().gameProgress < 100000){
                            SavedData.setGameProgress(100000);
                        }
                        game.loadNextScreen(KronosMenu.this, screens[aux.get()]);
                    }
                });

                if(statsData.gameProgress-1 >  i){
                    image.getColor().a = 0f;
                }
            }
            else {
                image = new Image(atlas.findRegion("locked"));
                image.setPosition(imagesPositions[i][0], imagesPositions[i][1]);
            }

            imagesTable.addActor(image);
        }

        TopTable topTable = new TopTable("KRONOS", "Selecione as 3 palavras corretas para cada imagem. " +
                "Tente carregar no olho para obter mais informação.", true, new TopTable.BackButtonCallback() {
            @Override
            public void onClicked() {
                Assets.clickFX.play();
                game.loadNextScreen(KronosMenu.this, KonnectingGame.ScreenType.MAIN_MENU);
            }
        });
        topTable.setPosition((mainStage.getWidth() - topTable.getWidth()) / 2,
                mainStage.getHeight() + mainStage.getPadBottom() - topTable.getHeight());
        mainStage.addActor(topTable);
        topTable.getScoreLabel().setText("" +
                (statsData.kronosScore1 + statsData.kronosScore2 + statsData.kronosScore3 +
                        statsData.kronosScore4 + statsData.kronosScore5 + statsData.kronosScore6 +
                statsData.kronosScore7) +"pt");
    }
}
