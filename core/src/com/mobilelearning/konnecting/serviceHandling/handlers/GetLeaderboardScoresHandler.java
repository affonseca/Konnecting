package com.mobilelearning.konnecting.serviceHandling.handlers;

import com.mobilelearning.konnecting.serviceHandling.json.LeaderboardScoresData;

/**
 * Created by AFFonseca on 23/07/2015.
 */
public interface GetLeaderboardScoresHandler {

    void onGetLeaderboardScoresSuccess(LeaderboardScoresData response);

    void onGetLeaderboardScoresClassError();

    void onGetLeaderboardScoresUserError();

    void onGetLeaderboardScoresError(String error);
}
