package com.example.thien.footballmanagement;

/**
 * Created by thien on 10/03/2018.
 */

public class RestPlayerService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private PlayerService apiService;

    public RestPlayerService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(PlayerService.class);
    }

    public PlayerService getService()
    {
        return apiService;
    }
}
