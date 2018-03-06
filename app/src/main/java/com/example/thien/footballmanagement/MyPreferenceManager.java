package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thien on 6/03/2018.
 */

public class MyPreferenceManager {
    Context context;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    private static final String PREF_NAME = "com.example.thien.footballmanagement";

    public static final String KEY_ACCESS_TOKEN = "access_token";

    public MyPreferenceManager(Context context) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        editor.apply();

    }

    public void putString(String key, String value) {

        editor.putString(key, value);

        editor.apply();

    }

    public String getString(String key) {

        return sharedPreferences.getString(key, null);

    }

    //Method to clear the login data of the application.
    public void clearLoginData() {
        editor.remove(KEY_ACCESS_TOKEN);
        editor.apply();

    }

}
