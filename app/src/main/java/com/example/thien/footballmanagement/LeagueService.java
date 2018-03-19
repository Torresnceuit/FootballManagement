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
    /**GET all leagues, return a list of leagues */
    @GET("/leagues/getall")
    public void getAllLeagues(/*@Header("Authorization") String token,*/ Callback<List<League>> callback);


    /**Get league record base on ID, return a league*/
    @GET("/leagues/getbyid/{id}")
    public void getLeagueById(@Path("id") String id,Callback<League> callback);


    /**Delete a league base on ID*/
    @DELETE("/leagues/delete/{id}")
    public void deleteLeague( @Path("id") String Id, Callback<List<League>> callback);


    /**POST a league record and post content in HTTP request BODY*/
    @POST("/leagues/update")
    public void updateLeague(@Body League league,Callback<League> callback);


}
