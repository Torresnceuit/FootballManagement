package com.example.thien.footballmanagement;

import android.widget.TextView;

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
 * TeamService Interface
 * GET all teams, GET all teams by tournamentId, GET a team Info base on ID
 * UPDATE a team
 * DELETE a team
 */

public interface TeamService {
    /**Get all teams, return a list of teams */
    @GET("/teams/getall")
    public void getAllTeams(Callback<List<Team>> callback);


    /**GET a team record base on ID, return a team*/
    @GET("/teams/getbyid/{id}")
    public void getTeamById(@Path("id") String id, Callback<Team> callback);

    /**GET all teams record base on tournamentId, return a list of teams*/
    @GET("/teams/getallbytour/{id}")
    public void getAllTeamsByTour( @Path("id") String id, Callback<List<Team>> callback);

    /**DELETE a team base on Id, return the list of existing teams*/
    @DELETE("/teams/delete/{id}")
    public void deleteTeam(@Path("id") String id, Callback<List<Team>> callback);


    /**POST a team record and post content in HTTP request BODY*/
    @POST("/teams/update")
    public void updateTeam(@Body Team team, Callback<Team> callback);
}
