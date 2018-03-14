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
 * Created by thien on 11/03/2018.
 */

public interface RankService {
    @GET("/ranks/getall")
    public void getAllRanks(@Header("Authorization") String token, Callback<List<Rank>> callback);


    //Get rank record base on ID
    @GET("/ranks/getbyid/{id}")
    public void getRankById(@Header("Authorization") String token, @Path("id") String id, Callback<Rank> callback);

    //Get ranks record base on leagueId
    @GET("/ranks/getallbytour/{id}")
    public void getAllRanksByTour(@Header("Authorization") String token, @Path("id") String id, Callback<List<Rank>> callback);

    
}
