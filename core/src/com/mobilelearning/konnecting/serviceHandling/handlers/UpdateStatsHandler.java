package com.mobilelearning.konnecting.serviceHandling.handlers;

/**
 * Created by AFFonseca on 16/07/2015.
 */
public interface UpdateStatsHandler {

    void onUpdateStatsSuccess();

    void onUpdateStatsClassError();

    void onUpdateStatsUserError();

    void onUpdateStatsOtherError(String error);

}
