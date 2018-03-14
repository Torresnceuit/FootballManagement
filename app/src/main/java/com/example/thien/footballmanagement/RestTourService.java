package com.example.thien.footballmanagement;

/**
 * Created by thien on 8/03/2018.
 */

public class RestTourService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private TournamentService apiService;

    public RestTourService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(TournamentService.class);
    }

    public TournamentService getService()
    {
        return apiService;
    }
}
