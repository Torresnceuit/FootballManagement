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
    /**GET all players, return a list of players*/
    @GET("/players/getall")
    public void getAllPlayers(Callback<List<Player>> callback);


    /**GET a player record base on ID, return a player*/
    @GET("/players/getbyid/{id}")
    public void getPlayerById(@Path("id") String id, Callback<Player> callback);

    /**GET all players record base on teamId, return a list of players*/
    @GET("/players/getallbyteam/{id}")
    public void getAllPlayersByTeam(@Path("id") String id, Callback<List<Player>> callback);

    /**DELETE a player, return a list of existing players*/
    @DELETE("/players/delete/{id}")
    public void deletePlayer(@Path("id") String id, Callback<List<Player>> callback);


    /**POST a player record and post content in HTTP request BODY*/
    @POST("/players/update")
    public void updatePlayer( @Body Player player, Callback<Player> callback);
}
