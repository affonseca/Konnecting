package com.mobilelearning.konnecting.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.KonnectingGame;

/**
 * Created by AFFonseca on 16/04/2015.
 */
public class StartupScreen extends StandardScreen {
    private TextureRegionDrawable backgroundImage;
    private TextureRegionDrawable button_normal, button_clicked;
    private Image alienImage;
    private Table symbolTable;

    public StartupScreen(KonnectingGame game, SpriteBatch batch) {
        super(game, batch);
    }

    @Override
    public void load() {
        Assets.loadStartupScreen();
    }

    @Override
    public void prepare() {
        super.prepare();
        TextureAtlas atlas = Assets.prepareStartupScreen();
        backgroundImage = new TextureRegionDrawable(atlas.findRegion("background"));
        button_normal = new TextureRegionDrawable(atlas.findRegion("button_start_up"));
        button_clicked = new TextureRegionDrawable(atlas.findRegion("button_start_down"));
        alienImage = new Image(atlas.findRegion("alien"));

        symbolTable = new Table();

        TextButton.TextButtonStyle symbolStyle = new TextButton.TextButtonStyle();
        symbolStyle.up = new TextureRegionDrawable(atlas.findRegion("symbol"));
        symbolStyle.font = Assets.uiSkin.getFont("default-font"); symbolStyle.fontColor = Color.WHITE;
        TextButton symbol = new TextButton("Konnecting", symbolStyle);

        Image symbolColor = new Image(atlas.findRegion("symbol_color"));
        symbolColor.setSize(symbol.getPrefWidth() * 0.65f, symbol.getPrefHeight() * 0.65f);
        symbol.addActorBefore(symbol.getLabel(), symbolColor);
        symbol.getLabel().setFontScale(0.75f);
        symbolTable.add(symbol)
                .size(symbol.getPrefWidth() * 0.65f, symbol.getPrefHeight() * 0.65f).row();

        Label textLabel = new Label("O Homem:\nser comunicante",
                new Label.LabelStyle(Assets.uiSkin.getFont("default-font"), Color.WHITE));
        textLabel.setAlignment(Align.center);
        textLabel.setFontScale(0.6f); symbolTable.add(textLabel);

    }

    @Override
    public void unload() {
        Assets.unloadStartupScreen();
    }

    @Override
    public void show() {
        super.show();

        backgroundStage.addActor(new Image(backgroundImage));

        symbolTable.setPosition(182f, 748f);
        mainStage.addActor(symbolTable);

        alienImage.setPosition(28f, -168f);
        mainStage.addActor(alienImage);

        Button startupButton = new Button(button_normal, button_clicked);
        startupButton.setPosition((mainStage.getWidth()-startupButton.getPrefWidth())/2, 24f);
        startupButton.addListener((new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                Assets.loadGlobalAssets();
                game.loadNextScreen(StartupScreen.this, KonnectingGame.ScreenType.MAIN_MENU);
            }
        }));


        mainStage.addActor(startupButton);
    }
}
