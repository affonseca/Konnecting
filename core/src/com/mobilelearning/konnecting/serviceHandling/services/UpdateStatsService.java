package com.mobilelearning.konnecting.serviceHandling.services;

import com.badlogic.gdx.utils.JsonValue;
import com.mobilelearning.konnecting.serviceHandling.Errors;
import com.mobilelearning.konnecting.serviceHandling.RequestException;
import com.mobilelearning.konnecting.serviceHandling.ServiceRequester;
import com.mobilelearning.konnecting.serviceHandling.handlers.UpdateStatsHandler;
import com.mobilelearning.konnecting.serviceHandling.json.StatsData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AFFonseca on 16/07/2015.
 */
public class UpdateStatsService implements Service{
    private UpdateStatsHandler handler;


    public UpdateStatsService(UpdateStatsHandler handler) {
        this.handler = handler;
    }

    public void requestUpdateStats(long classID, StatsData data) {

        Map<String, String> parameters = new HashMap<>();

        parameters.put("classID", "" +classID);
        parameters.put("gameProgress", "" +data.gameProgress);
        parameters.put("kronosScore1", "" +data.kronosScore1);
        parameters.put("kronosScore2", "" +data.kronosScore2);
        parameters.put("kronosScore3", "" +data.kronosScore3);
        parameters.put("kronosScore4", "" +data.kronosScore4);
        parameters.put("kronosScore5", "" +data.kronosScore5);
        parameters.put("kronosScore6", "" +data.kronosScore6);
        parameters.put("kronosScore7", "" +data.kronosScore7);
        parameters.put("zappingScore1", "" +data.zappingScore1);
        parameters.put("zappingScore2", "" +data.zappingScore2);
        parameters.put("zappingScore3", "" +data.zappingScore3);
        parameters.put("zappingScore4", "" +data.zappingScore4);
        parameters.put("zappingScore5", "" +data.zappingScore5);

        try{
            ServiceRequester serviceRequester = new ServiceRequester(this, parameters);
            serviceRequester.postRequest();
        }
        catch (RequestException e){
            onRequestFailure(Errors.MAX_CONCURRENT_REQUESTS_REACHED.getValue());
        }
    }


    @Override
    public void onRequestSuccess(JsonValue response) {
        handler.onUpdateStatsSuccess();
    }

    @Override
    public void onRequestFailure(String error) {
        switch (error) {
            case "A turma selecionada já não existe":
                handler.onUpdateStatsClassError();
                break;
            case "Já não pertences à turma selecionada":
                handler.onUpdateStatsUserError();
                break;
            default:
                handler.onUpdateStatsOtherError(error);
        }
    }

    @Override
    public String getType() {
        return ServiceType.UPDATE_STATS.getValue();
    }
}
