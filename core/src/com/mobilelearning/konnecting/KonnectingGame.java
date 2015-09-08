package com.mobilelearning.konnecting;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mobilelearning.konnecting.screens.EndingScreen;
import com.mobilelearning.konnecting.screens.Kronos;
import com.mobilelearning.konnecting.screens.KronosMenu;
import com.mobilelearning.konnecting.screens.MainMenu;
import com.mobilelearning.konnecting.screens.StandardScreen;
import com.mobilelearning.konnecting.screens.StartupScreen;
import com.mobilelearning.konnecting.screens.WelcomeScreen;
import com.mobilelearning.konnecting.screens.Zapping;
import com.mobilelearning.konnecting.screens.ZappingMenu;

public class KonnectingGame extends Game {
	private StandardScreen newScreen;
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		//Loading assets and saved data
		Assets.loadStartupAssets();
		SavedData.loadSavedData();

		//Starting with main menu
		StandardScreen startup = new StartupScreen(this, batch);
		startup.prepare();
		setScreen(startup);
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
		batch.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	public void loadNextScreen(final StandardScreen oldScreen, ScreenType newScreenType){

		switch (newScreenType){
			case STARTUP:
				newScreen = new StartupScreen(this, batch);
				break;
			case MAIN_MENU:
				newScreen = new MainMenu(this, batch);
				break;
			case WELCOME_SCREEN:
				newScreen = new WelcomeScreen(this, batch);
				break;
			case KRONOS_MENU:
				newScreen = new KronosMenu(this, batch);
				break;
			case KRONOS1:
				newScreen = new Kronos(this, batch, 1);
				break;
            case KRONOS2:
                newScreen = new Kronos(this, batch, 2);
                break;
            case KRONOS3:
                newScreen = new Kronos(this, batch, 3);
                break;
            case KRONOS4:
                newScreen = new Kronos(this, batch, 4);
                break;
            case KRONOS5:
                newScreen = new Kronos(this, batch, 5);
                break;
            case KRONOS6:
                newScreen = new Kronos(this, batch, 6);
                break;
            case KRONOS7:
                newScreen = new Kronos(this, batch, 7);
                break;
			case ZAPPING_MENU:
				newScreen = new ZappingMenu(this, batch);
				break;
			case ZAPPING1:
				newScreen = new Zapping(this, batch, 1);
				break;
			case ZAPPING2:
				newScreen = new Zapping(this, batch, 2);
				break;
			case ZAPPING3:
				newScreen = new Zapping(this, batch, 3);
				break;
			case ZAPPING4:
				newScreen = new Zapping(this, batch, 4);
				break;
			case ZAPPING5:
				newScreen = new Zapping(this, batch, 5);
				break;
			case ENDING_SCREEN:
				newScreen = new EndingScreen(this, batch);
				break;
			default:
				throw  new IllegalArgumentException("No valid screen type!");
		}

		newScreen.load();
		oldScreen.startLoadingAnimation(true);
	}

	public void changeScreen(final StandardScreen oldScreen){
		oldScreen.stopLoadingAnimation();
		newScreen.prepare();
		setScreen(newScreen);
		oldScreen.unload();
		oldScreen.dispose();
	}

	public enum ScreenType{
		STARTUP, MAIN_MENU, WELCOME_SCREEN, KRONOS_MENU,
        KRONOS1, KRONOS2, KRONOS3, KRONOS4, KRONOS5, KRONOS6, KRONOS7,
        ZAPPING_MENU, ZAPPING1, ZAPPING2, ZAPPING3, ZAPPING4, ZAPPING5, ENDING_SCREEN
	}
}
