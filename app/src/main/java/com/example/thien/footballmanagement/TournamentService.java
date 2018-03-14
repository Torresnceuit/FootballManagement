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
 * Created by thien on 8/03/2018.
 * TournamentService Interface
 * Build the methods to
 * GET tournaments from leagueId, tournamentId
 * DELETE a tournament
 * UPDATE a tournament
 * Generate a fixture
 * Generate a rank
 */

public interface TournamentService {
    //i.e. http://localhost/api/tournaments/getall
    @GET("/tournaments/getall")
    public void getAllTours(@Header("Authorization") String token, Callback<List<Tournament>> callback);


    //GET tour record base on ID
    @GET("/tournaments/getbyid/{id}")
    public void getTourById(@Header("Authorization") String token, @Path("id") String id, Callback<Tournament> callback);

    //GET tour record base on leagueId
    @GET("/tournaments/getallbyleague/{id}")
    public void getAllToursByLeague(@Header("Authorization") String token, @Path("id") String id, Callback<List<Tournament>> callback);

    //DELETE tour base on ID
    @DELETE("/tournaments/delete/{id}")
    public void deleteTour(@Header("Authorization") String token, @Path("id") String id, Callback<List<Tournament>> callback);


    //POST tour record and post content in HTTP request BODY
    @POST("/tournaments/update")
    public void updateTour(@Header("Authorization") String token,@Body Tournament tour,Callback<Tournament> callback);

    ///POST Generate fixture for tournament
    @POST("/generator/drawfixture/{id}")
    public void generateFixture(@Header("Authorization") String token,@Path("id") String id,Callback<Object> callback);

    ///POST Generate rank for tournament
    @POST("/generator/generateRank/{id}")
    public void generateRank(@Header("Authorization") String token,@Path("id") String id,Callback<Object> callback);
}
