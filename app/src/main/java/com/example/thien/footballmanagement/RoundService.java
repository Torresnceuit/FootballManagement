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
    //GET all rounds
    @GET("/rounds/getall")
    public void getAllRounds(@Header("Authorization") String token, Callback<List<Round>> callback);


    //GET round record base on ID
    @GET("/rounds/getbyid/{id}")
    public void getRoundById(@Header("Authorization") String token, @Path("id") String id, Callback<Round> callback);

    //GET round record base on tournamentId
    @GET("/rounds/getallbytour/{id}")
    public void getAllRoundsByTour(@Header("Authorization") String token, @Path("id") String id, Callback<List<Round>> callback);

    //DELETE a round
    @DELETE("/rounds/delete/{id}")
    public void deleteRound(@Header("Authorization") String token, @Path("id") String id, Callback<List<Round>> callback);


    //POST round record and post content in HTTP request BODY
    @POST("/rounds/update")
    public void updateRound(@Header("Authorization") String token, @Body Round round, Callback<Round> callback);
}
