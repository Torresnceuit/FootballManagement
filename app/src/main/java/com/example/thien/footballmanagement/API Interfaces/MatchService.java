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
 * Created by thien on 10/03/2018.
 * MatchService Interface
 * GET all matches, match by Id and all matches by round Id
 * UPDATE a match, all matches
 * DELETE a match
 */

public interface MatchService {
    /**GET all matches, return a list of matches */
    @GET("/matches/getall")
    public void getAllMatches( Callback<List<Match>> callback);


    /**GET match record base on ID*/
    @GET("/matches/getbyid/{id}")
    public void getMatchById(@Path("id") String id, Callback<Match> callback);

    /**GET all matches record base on teamId*/
    @GET("/matches/getallbyround/{id}")
    public void getAllMatchesByRound(@Path("id") String id, Callback<List<Match>> callback);

    /**DELETE a match*/
    @DELETE("/matches/delete/{id}")
    public void deleteMatch(@Path("id") String id, Callback<List<Match>> callback);


    /**POST a match record and post content in HTTP request BODY*/
    @POST("/matches/update")
    public void updateMatch(@Body Match match, Callback<Match> callback);

    /**POST update all matches*/
    @POST("/matches/updateall")
    public void proceedMatches(@Body List<Match> matches, Callback<Object> callback);
}
