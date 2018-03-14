package com.example.thien.footballmanagement;

/**
 * Created by thien on 9/03/2018.
 */

public class RestRoundService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private RoundService apiService;

    public RestRoundService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(RoundService.class);
    }

    public RoundService getService()
    {
        return apiService;
    }
}
