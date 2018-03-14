package com.example.thien.footballmanagement;

import android.os.RemoteCallbackList;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
/**
 * Created by thien on 7/03/2018.
 * LeagueService Interface
 * GET all leagues
 * GET leagues by Id
 * UPDATE a league
 * DELETE a league
 */

public interface LeagueService {
    //i.e. http://localhost/api/leagues/getall
    @GET("/leagues/getall")
    public void getAllLeagues(@Header("Authorization") String token, Callback<List<League>> callback);


    //Get league record base on ID
    @GET("/leagues/getbyid/{id}")
    public void getLeagueById(@Header("Authorization") String token, @Path("id") String id,Callback<League> callback);


    //Delete league base on ID
    @DELETE("/leagues/delete/{id}")
    public void deleteLeague(@Header("Authorization") String token, @Path("id") String Id, Callback<List<League>> callback);


    //POST league record and post content in HTTP request BODY
    @POST("/leagues/update")
    public void updateLeague(@Header("Authorization") String token, @Body League league,Callback<League> callback);


}
