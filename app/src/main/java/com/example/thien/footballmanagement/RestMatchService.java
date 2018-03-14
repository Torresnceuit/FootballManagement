package com.example.thien.footballmanagement;

/**
 * Created by thien on 10/03/2018.
 */

public class RestMatchService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private MatchService apiService;

    public RestMatchService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(MatchService.class);
    }

    public MatchService getService()
    {
        return apiService;
    }
}
