package com.mobilelearning.konnecting.serviceHandling.handlers;

import com.mobilelearning.konnecting.serviceHandling.json.StatsData;

/**
 * Created by AFFonseca on 16/07/2015.
 */
public interface GetStatsHandler {

    void onGetStatsSuccess(StatsData response);
    void onGetStatsError(String error);

}
