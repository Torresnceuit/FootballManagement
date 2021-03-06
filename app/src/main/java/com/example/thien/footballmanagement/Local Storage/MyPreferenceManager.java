package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thien on 6/03/2018.
 * Application Local Storage
 * For GET and PUT data
 */

public class MyPreferenceManager {
    Context context;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    private static final String PREF_NAME = "com.example.thien";

    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    // constructor
    public MyPreferenceManager(Context context) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        editor.apply();

    }
    // store a string in key, value to Preferences
    public void putString(String key, String value) {

        editor.putString(key, value);

        editor.apply();

    }
    // get a string with key from Preferences
    public String getString(String key) {

        return sharedPreferences.getString(key, null);

    }

    //Method to clear the login data of the application.
    public void clearLoginData() {
        editor.remove(KEY_ACCESS_TOKEN);
        editor.apply();

    }

}
