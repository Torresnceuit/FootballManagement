package com.example.thien.footballmanagement;

import com.jakewharton.retrofit.Ok3Client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by thien on 10/03/2018.
 */

public class RestMatchService {
    private retrofit.RestAdapter restAdapter;
    private MatchService apiService;
    private String bearer = "";


    public RestMatchService()
    {
        // get bearer from local storage
        bearer = MyApp.getInstance().getPreferenceManager().getString(MyPreferenceManager.KEY_ACCESS_TOKEN);

        // build request with interceptor
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + bearer)
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(MyApp.REQ_URL)
                .setClient(new Ok3Client(httpClient))
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(MatchService.class);
    }

    public MatchService getService()
    {
        return apiService;
    }
}
