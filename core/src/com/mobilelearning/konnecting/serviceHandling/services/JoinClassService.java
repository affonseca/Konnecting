package com.mobilelearning.konnecting.serviceHandling.services;

import com.badlogic.gdx.utils.JsonValue;
import com.mobilelearning.konnecting.serviceHandling.Errors;
import com.mobilelearning.konnecting.serviceHandling.RequestException;
import com.mobilelearning.konnecting.serviceHandling.ServiceRequester;
import com.mobilelearning.konnecting.serviceHandling.handlers.JoinClassHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: AFFonseca
 * Date: 26-01-2015
 * Time: 18:36
 * To change this template use File | Settings | File Templates.
 */
public class JoinClassService implements Service {

    private JoinClassHandler handler;


    public JoinClassService(JoinClassHandler handler) {
        this.handler = handler;
    }

    public void requestJoinClass(String classID) {

        Map<String, String> parameters = new HashMap<>();

        parameters.put("classID", classID);

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

        handler.onJoinClassSuccess();

    }

    @Override
    public void onRequestFailure(String error) {
        handler.onJoinClassError(error);
    }

    @Override
    public String getType() {
        return ServiceType.JOIN_CLASS.getValue();
    }
}
