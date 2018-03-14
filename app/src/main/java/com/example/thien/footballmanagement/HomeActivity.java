package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends AppCompatActivity {

    //List of League is used to store the leagues received from response
    private List<League> leagues;
    private final String TAG = "HomeActivity";
    /*We always need to get this activity context when we want to load
    /
    / sharePreference fron non-activity class to get bearer for request.
    / contextOfApplication will help us to refer to get content of SharePreferences
    */
    public static Context contextOfApplication;
    public static final String MYPREF = "com.example.thien";
    private SharedPreferences sharedPreferences;
    public static String bearer = "";

    ListView listView;
    Button btnAdd;
    // The Rest Service call to LeagueService Interface
    RestLeagueService restLeagueService;

    // league_Id will be passed to new Intent to refer back to the league instance
    TextView league_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Initiate all variable here*/
        restLeagueService = new RestLeagueService();
        contextOfApplication = getApplicationContext();
        btnAdd= (Button) findViewById(R.id.btnAdd);


        sharedPreferences = getSharedPreferences(MYPREF,MODE_PRIVATE);
        bearer = "Bearer "+sharedPreferences
                .getString("access_token","");//Get the access_token stored in sharedPreference after logging in
        Log.d(TAG,"OnCreate");
        Log.d(TAG,bearer);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd();
            }
        });





    }

    @Override
    public void onResume() {

        super.onResume();
        refreshScreen();
    }
    //// OPEN ADD LEAGUE DIALOG
    private void openAdd(){
        Intent i = new Intent(getApplicationContext(),AddLeague.class);
        startActivity(i);
    }


    //// TO DO when refreshing screen
    public void refreshScreen() {

        //Call to server to grab list of student records. this is a asyn
        restLeagueService.getService().getAllLeagues(bearer,new Callback<List<League>>() {

            //Successful response: render listview by LeagueAdapter
            @Override
            public void success(List<League> leagues, Response response) {
                ListView lv = (ListView) findViewById(R.id.listView);

                Log.d(TAG,"GetAllLeagues success");

                LeagueAdapter leagueAdapter = new LeagueAdapter(HomeActivity.this, R.layout.view_league_entry, leagues);

                //set onClick event on list item
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        league_Id = (TextView) view.findViewById(R.id.league_Id);
                        String leagueId = league_Id.getText().toString();

                        //Open new LeagueDetail Activity with the league_Id as extra string
                        Intent objIndent = new Intent(getApplicationContext(), LeagueDetail.class);
                        objIndent.putExtra("league_Id", leagueId);
                        startActivity(objIndent);

                    }
                });
                lv.setAdapter(leagueAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"GetAllLeagues failed");
                Toast.makeText(HomeActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Get the HomeActivity Context
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }




}
