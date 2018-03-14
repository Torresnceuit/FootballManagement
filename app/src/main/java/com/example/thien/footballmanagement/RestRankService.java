package com.example.thien.footballmanagement;

/**
 * Created by thien on 11/03/2018.
 */

public class RestRankService {
    private static final String URL = "http://10.0.2.2:55903/api/";
    private retrofit.RestAdapter restAdapter;
    private RankService apiService;

    public RestRankService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(RankService.class);
    }

    public RankService getService()
    {
        return apiService;
    }
}
