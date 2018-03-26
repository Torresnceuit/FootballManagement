package com.example.thien.footballmanagement;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by thien on 9/03/2018.
 * RoundService Interface
 * GET all rounds, a round by round Id, all rounds by tournament Id
 * UPDATE a round
 * DELETE a round
 */

public interface RoundService {
    /**GET all rounds, return a list of rounds*/
    @GET("/rounds/getall")
    public void getAllRounds( Callback<List<Round>> callback);


    /**GET a round record base on ID, return a round*/
    @GET("/rounds/getbyid/{id}")
    public void getRoundById(@Path("id") String id, Callback<Round> callback);

    /**GET all rounds record base on tournamentId, return a list of rounds*/
    @GET("/rounds/getallbytour/{id}")
    public void getAllRoundsByTour(@Path("id") String id, Callback<List<Round>> callback);

    /**DELETE a round, return a list of existing rounds*/
    @DELETE("/rounds/delete/{id}")
    public void deleteRound(@Path("id") String id, Callback<List<Round>> callback);


    /**POST round record and post content in HTTP request BODY*/
    @POST("/rounds/update")
    public void updateRound(@Body Round round, Callback<Round> callback);
}
