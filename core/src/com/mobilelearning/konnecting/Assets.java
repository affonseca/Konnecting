package com.mobilelearning.konnecting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.mobilelearning.konnecting.screens.Kronos;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created with IntelliJ IDEA.
 * User: AFFonseca
 * Date: 12-01-2015
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class Assets {

    public static final String currentVersion = "1.0.1";

    public static final int numberOfImages = 82;
    public static Kronos.ExerciseInfoArray exercisesData;

    //Global Assets
    public static Skin uiSkin;
    public static TextureAtlas miscellaneous;
    public static Animation loading;

    //Sound Assets
    public static Sound clickFX;
    public static Sound successFX;
    public static Sound failFX;
    public static Sound moneyFX;
    public static Music fanfareFX;

    private static GameAssetManager manager;

    public static void  loadStartupAssets(){
        manager = new GameAssetManager();

        manager.gLoad("ui/startup.atlas", TextureAtlas.class);
        manager.gLoad("ui/uiskin.json", Skin.class);
        manager.gLoad("animations/loadingAnimation.atlas", TextureAtlas.class);
        manager.gLoad("sounds/clickFX.ogg", Sound.class);

        manager.finishLoading();

        final TextureAtlas loadingAtlas = manager.get("animations/loadingAnimation.atlas", TextureAtlas.class);
        loadingAtlas.getRegions().sort(new Comparator<TextureAtlas.AtlasRegion>() {
            @Override
            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        loading = new Animation(1 / 15f, loadingAtlas.getRegions());
        loading.setPlayMode(Animation.PlayMode.LOOP);
        uiSkin = manager.get("ui/uiskin.json", Skin.class);
        clickFX = manager.get("sounds/clickFX.ogg", Sound.class);

    }

    public static void loadGlobalAssets() {
        //basic elements
        manager.gLoad("ui/miscellaneous.atlas", TextureAtlas.class);

        //UI
        manager.gLoad("ui/mainMenu.atlas", TextureAtlas.class);
        manager.gLoad("ui/kronosMenu.atlas", TextureAtlas.class);
        manager.gLoad("ui/kronos.atlas", TextureAtlas.class);
        manager.gLoad("ui/images/kronos_big.atlas", TextureAtlas.class);
        manager.gLoad("ui/images/kronos_time.atlas", TextureAtlas.class);
        manager.gLoad("ui/zappingMenu.atlas", TextureAtlas.class);
        manager.gLoad("ui/zapping.atlas", TextureAtlas.class);
        manager.gLoad("ui/images/zapping.atlas", TextureAtlas.class);

        //Sound
        manager.gLoad("sounds/moneyFX.ogg", Sound.class);
        manager.gLoad("sounds/fanfareFX.ogg", Music.class);
        manager.gLoad("sounds/successFX.ogg", Sound.class);
        manager.gLoad("sounds/failFX.ogg", Sound.class);
    }

    public static void prepareGlobalAssets(){
        miscellaneous = manager.get("ui/miscellaneous.atlas", TextureAtlas.class);

        exercisesData = new Json().fromJson(Kronos.ExerciseInfoArray.class,
                Gdx.files.internal("exerciseInfo.json").readString());


        //Sounds
        moneyFX = manager.get("sounds/moneyFX.ogg", Sound.class);
        fanfareFX = manager.get("sounds/fanfareFX.ogg", Music.class);
        successFX = manager.get("sounds/successFX.ogg", Sound.class);
        failFX = manager.get("sounds/failFX.ogg", Sound.class);

    }

    public static void loadStartupScreen(){
        manager.gLoad("ui/startup.atlas", TextureAtlas.class);
    }

    public static TextureAtlas prepareStartupScreen(){
        return manager.get("ui/startup.atlas", TextureAtlas.class);
    }

    public static void unloadStartupScreen(){
        manager.gUnload("ui/startup.atlas");
    }

    public static TextureAtlas prepareMainMenu(){
        return manager.get("ui/mainMenu.atlas", TextureAtlas.class);
    }

    public static void loadWelcomeScreen(){
        manager.gLoad("ui/welcomeScreen.atlas", TextureAtlas.class);
    }

    public static TextureAtlas prepareWelcomeScreen(){
        return manager.get("ui/welcomeScreen.atlas", TextureAtlas.class);
    }

    public static void unloadWelcomeScreen(){
        manager.gUnload("ui/welcomeScreen.atlas");
    }

    public static TextureAtlas prepareKronosMenu() {
        return manager.get("ui/kronosMenu.atlas", TextureAtlas.class);
    };

    public static TextureAtlas [] prepareKronos(){
        return new TextureAtlas[] {
                manager.get("ui/kronos.atlas", TextureAtlas.class),
                manager.get("ui/images/kronos_big.atlas", TextureAtlas.class),
                manager.get("ui/images/kronos_time.atlas", TextureAtlas.class)
        };
    }

    public static TextureAtlas prepareZappingMenu(){
        return manager.get("ui/zappingMenu.atlas", TextureAtlas.class);
    }

    public static TextureAtlas [] prepareZapping(){
        return new TextureAtlas[] {
                manager.get("ui/zapping.atlas", TextureAtlas.class),
                manager.get("ui/images/zapping.atlas", TextureAtlas.class)
        };
    }

    public static void loadEndingScreen(){
        manager.gLoad("ui/endingScreen.atlas", TextureAtlas.class);
    }

    public static TextureAtlas prepareEndingScreen(){
        return manager.get("ui/endingScreen.atlas", TextureAtlas.class);
    }

    public static void unloadEndingScreen(){
        manager.gUnload("ui/endingScreen.atlas");
    }

    public static boolean update(){
        return manager.update();
    }

    public static void dispose(){
        if(manager != null)
            manager.dispose();
    }

    public static class GameAssetManager extends AssetManager {
        ObjectMap<String, AtomicInteger> usages;

        public GameAssetManager() {
            super();
            usages = new ObjectMap<>();
        }

        public synchronized <T> void gLoad(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
            if(!usages.containsKey(fileName)){
                usages.put(fileName, new AtomicInteger(0));
                super.load(fileName, type, parameter);
            }
            usages.get(fileName).addAndGet(1);
        }

        public synchronized <T> void gLoad(String fileName, Class<T> type) {
            gLoad(fileName, type, null);
        }

        public synchronized void gUnload(String fileName) {
            if(usages.containsKey(fileName)){
                if(usages.get(fileName).addAndGet(-1) == 0){
                    usages.remove(fileName);
                    super.unload(fileName);
                }
            }
        }
    }

}
