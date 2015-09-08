package com.mobilelearning.konnecting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mobilelearning.konnecting.serviceHandling.json.*;

/**
 * Created with IntelliJ IDEA.
 * User: AFFonseca
 * Date: 26-01-2015
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class SavedData {

    private enum Values{

        DATA_NAME("MobileLearning"),
        TOKEN("token"),
        USERNAME("username"),
        USER_ID("userID"),
        CURRENT_CLASS_NAME("currentClassName"),
        CURRENT_CLASS_TEACHER("currentClassTeacher"),
        CURRENT_CLASS_ID("currentClassID"),
        GAME_PROGRESS("gameProgress"),
        KRONOS_SCORE1("kronosScore1"), KRONOS_SCORE2("kronosScore2"),
        KRONOS_SCORE3("kronosScore3"), KRONOS_SCORE4("kronosScore4"),
        KRONOS_SCORE5("kronosScore5"), KRONOS_SCORE6("kronosScore6"),
        KRONOS_SCORE7("kronosScore7"), ZAPPING_SCORE1("zappingScore1"),
        ZAPPING_SCORE2("zappingScore2"), ZAPPING_SCORE3("zappingScore3"),
        ZAPPING_SCORE4("zappingScore4"), ZAPPING_SCORE5("zappingScore5");

        private String value;

        Values(String value) {
            this.value = value;
        }

        private String getValue(){
            return value;
        }

    }

    private static Preferences userData;

    public static void loadSavedData(){
        userData = Gdx.app.getPreferences(Values.DATA_NAME.getValue());
    }

    public static String getToken(){
        return userData.getString(Values.TOKEN.getValue(), null);
    }

    public static String getUsername(){
        return userData.getString(Values.USERNAME.getValue(), null);
    }

    public static String getUserID(){
        return userData.getString(Values.USER_ID.getValue(), null);
    }

    public static String getCurrentClassName(){
        return userData.getString(Values.CURRENT_CLASS_NAME.getValue(), null);
    }

    public static String getCurrentClassTeacher(){
        return userData.getString(Values.CURRENT_CLASS_TEACHER.getValue(), null);
    }

    public static String getCurrentClassID(){
        return userData.getString(Values.CURRENT_CLASS_ID.getValue(), null);
    }

    public static void setUserData(UserData user){
        userData.putString(Values.USER_ID.getValue(), "" +user.ID);
        userData.putString(Values.USERNAME.getValue(), "" +user.username);
        userData.putString(Values.TOKEN.getValue(), "" +user.hash);
        userData.flush();
    }

    public static void setCurrentClass(ClassInfo newClass){
        userData.putString(Values.CURRENT_CLASS_ID.getValue(), "" +newClass.classID);
        userData.putString(Values.CURRENT_CLASS_NAME.getValue(), "" +newClass.className);
        userData.putString(Values.CURRENT_CLASS_TEACHER.getValue(), "" + newClass.classTeacher);
        userData.flush();
    }

    public static void setGameProgress(int gameProgress){
        userData.putInteger(Values.GAME_PROGRESS.getValue(), gameProgress);
        userData.flush();
    }

    public static void setStats(StatsData stats){
        userData.putInteger(Values.GAME_PROGRESS.getValue(), stats.gameProgress);

        userData.putInteger(Values.KRONOS_SCORE1.getValue(), stats.kronosScore1);
        userData.putInteger(Values.KRONOS_SCORE2.getValue(), stats.kronosScore2);
        userData.putInteger(Values.KRONOS_SCORE3.getValue(), stats.kronosScore3);
        userData.putInteger(Values.KRONOS_SCORE4.getValue(), stats.kronosScore4);
        userData.putInteger(Values.KRONOS_SCORE5.getValue(), stats.kronosScore5);
        userData.putInteger(Values.KRONOS_SCORE6.getValue(), stats.kronosScore6);
        userData.putInteger(Values.KRONOS_SCORE7.getValue(), stats.kronosScore7);

        userData.putInteger(Values.ZAPPING_SCORE1.getValue(), stats.zappingScore1);
        userData.putInteger(Values.ZAPPING_SCORE2.getValue(), stats.zappingScore2);
        userData.putInteger(Values.ZAPPING_SCORE3.getValue(), stats.zappingScore3);
        userData.putInteger(Values.ZAPPING_SCORE4.getValue(), stats.zappingScore4);
        userData.putInteger(Values.ZAPPING_SCORE5.getValue(), stats.zappingScore5);

        userData.flush();
    }

    public static StatsData getStats(){
        StatsData out = new StatsData();

        out.gameProgress = userData.getInteger(Values.GAME_PROGRESS.getValue(), 0);

        out.kronosScore1= userData.getInteger(Values.KRONOS_SCORE1.getValue(), 0);
        out.kronosScore2= userData.getInteger(Values.KRONOS_SCORE2.getValue(), 0);
        out.kronosScore3= userData.getInteger(Values.KRONOS_SCORE3.getValue(), 0);
        out.kronosScore4= userData.getInteger(Values.KRONOS_SCORE4.getValue(), 0);
        out.kronosScore5= userData.getInteger(Values.KRONOS_SCORE5.getValue(), 0);
        out.kronosScore6= userData.getInteger(Values.KRONOS_SCORE6.getValue(), 0);
        out.kronosScore7= userData.getInteger(Values.KRONOS_SCORE7.getValue(), 0);

        out.zappingScore1= userData.getInteger(Values.ZAPPING_SCORE1.getValue(), 0);
        out.zappingScore2= userData.getInteger(Values.ZAPPING_SCORE2.getValue(), 0);
        out.zappingScore3= userData.getInteger(Values.ZAPPING_SCORE3.getValue(), 0);
        out.zappingScore4= userData.getInteger(Values.ZAPPING_SCORE4.getValue(), 0);
        out.zappingScore5= userData.getInteger(Values.ZAPPING_SCORE5.getValue(), 0);

        return out;
    }

    public static void clearUserData(){
        userData.clear();
        userData.flush();
    }

}
