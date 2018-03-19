package com.example.thien.footballmanagement;

import android.app.Application;
import android.util.Log;

/**
 * Created by thien on 6/03/2018.
 */

public class MyApp extends Application {
    public static MyApp myApp ;
    public static MyPreferenceManager myPreferenceManager;
    public static final String REQ_URL = "http://10.0.2.2:55903/api";

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = this;

    }

    /*For creating the context of the Whole app.*/
    public static MyApp getInstance()
    {
        if(myApp==null){
            myApp = new MyApp();
        }

        return myApp ;
    }

    /*This is for getting the instance of the MyPreferenceManager class using the context of MyApp App.*/
    public MyPreferenceManager getPreferenceManager() {
        if(getInstance().getApplicationContext()!=null){
            Log.d("MyApp","getInstance()!= null");
        }
        if (myPreferenceManager == null) {
            myPreferenceManager = new MyPreferenceManager(getInstance().getApplicationContext());
        }

        return myPreferenceManager;
    }

}
