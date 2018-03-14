package com.example.thien.footballmanagement;

/**
 * Created by thien on 7/03/2018.
 */

public class RestLeagueService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private LeagueService apiService;

    public RestLeagueService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(LeagueService.class);
    }

    public LeagueService getService()
    {
        return apiService;
    }
}
