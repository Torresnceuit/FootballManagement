package com.example.thien.footballmanagement;

import com.jakewharton.retrofit.Ok3Client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by thien on 9/03/2018.
 */

public class RestRoundService {
    private retrofit.RestAdapter restAdapter;
    private RoundService apiService;
    private String bearer = "";


    public RestRoundService()
    {
        // get bearer
        bearer = MyApp.getInstance().getPreferenceManager().getString(MyPreferenceManager.KEY_ACCESS_TOKEN);

        // include interceptor in request
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

        apiService = restAdapter.create(RoundService.class);
    }

    public RoundService getService()
    {
        return apiService;
    }
}
