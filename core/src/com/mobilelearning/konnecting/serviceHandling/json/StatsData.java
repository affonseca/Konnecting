package com.mobilelearning.konnecting.serviceHandling.json;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.SerializationException;
import com.mobilelearning.konnecting.Assets;

/**
 * Created by AFFonseca on 16/07/2015.
 */
public class StatsData {
    public int gameProgress;
    public int kronosScore1, kronosScore2, kronosScore3,
            kronosScore4, kronosScore5, kronosScore6, kronosScore7;
    public int zappingScore1, zappingScore2, zappingScore3, zappingScore4, zappingScore5;

    public static StatsData load (JsonValue fatherTree) {
        try {
            Json json = new Json();
            return json.readValue(StatsData.class, fatherTree.get("statsData"));
        } catch (Exception ex) {
            throw new SerializationException("Error reading scoreData from Json" , ex);
        }
    }
}
