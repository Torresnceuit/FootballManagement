package com.example.thien.footballmanagement;

import com.jakewharton.retrofit.Ok3Client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * Created by thien on 7/03/2018.
 */

public class RestLeagueService {
    private retrofit.RestAdapter restAdapter;
    private LeagueService apiService;
    private String bearer = "";


    public RestLeagueService()
    {
        // get bearer from local storage
        bearer = MyApp.getInstance().getPreferenceManager().getString(MyPreferenceManager.KEY_ACCESS_TOKEN);
        // Use OkHttpClient with Interceptor
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + bearer)
                                .method(original.method(), original.body());
                        
                        // build request
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();
                
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(MyApp.REQ_URL)
                .setClient(new Ok3Client(httpClient))
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(LeagueService.class);
    }

    public LeagueService getService()
    {
        return apiService;
    }
}
