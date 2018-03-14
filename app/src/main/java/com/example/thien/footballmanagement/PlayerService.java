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
 * PlayerService Interface
 * GET all players, a player by Id, all players by team Id
 * UPDATE a player
 * DELETE a player
 */

public interface PlayerService {
    //GET all players
    @GET("/players/getall")
    public void getAllPlayers(@Header("Authorization") String token, Callback<List<Player>> callback);


    //GET player record base on ID
    @GET("/players/getbyid/{id}")
    public void getPlayerById(@Header("Authorization") String token, @Path("id") String id, Callback<Player> callback);

    //GET players record base on teamId
    @GET("/players/getallbyteam/{id}")
    public void getAllPlayersByTeam(@Header("Authorization") String token, @Path("id") String id, Callback<List<Player>> callback);

    //DELETE a player
    @DELETE("/players/delete/{id}")
    public void deletePlayer(@Header("Authorization") String token, @Path("id") String id, Callback<List<Player>> callback);


    //POST player record and post content in HTTP request BODY
    @POST("/players/update")
    public void updatePlayer(@Header("Authorization") String token, @Body Player player, Callback<Player> callback);
}
