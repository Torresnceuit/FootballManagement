package com.example.thien.footballmanagement;

/**
 * Created by thien on 9/03/2018.
 */

public class RestTeamService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private TeamService apiService;

    public RestTeamService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(TeamService.class);
    }

    public TeamService getService()
    {
        return apiService;
    }
}
