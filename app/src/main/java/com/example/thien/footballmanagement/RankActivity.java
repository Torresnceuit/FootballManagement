package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RankActivity extends AppCompatActivity {

    private List<Match> matches;
    private String TAG = "RoundDetailActivity";
    public static final String MYPREF = "com.example.thien";
    public SharedPreferences sharedPreferences;
    public static String bearer = "";
    public static Context contextOfApplication;
    RestRankService restRankService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        restRankService = new RestRankService();
        contextOfApplication = getApplicationContext();
        sharedPreferences = getSharedPreferences(MYPREF,MODE_PRIVATE);
        bearer = "Bearer "+sharedPreferences.getString("access_token","");
        Log.d(TAG,"OnCreate");
        Log.d(TAG,bearer);
    }

    @Override
    public void onResume() {

        super.onResume();
        refreshScreen();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private void refreshScreen() {

        String tour_Id="";
        Intent i = getIntent();
        tour_Id = i.getStringExtra("tour_Id");
        /// GET ALL RANKS FROM TOUR
        restRankService.getService().getAllRanksByTour(bearer, tour_Id, new Callback<List<Rank>>() {
            @Override
            public void success(List<Rank> ranks, Response response) {
                //ListView lv = (ListView) findViewById(R.id.listView);
                TableView<Rank> tv = (TableView<Rank>) findViewById(R.id.tableView);


                Log.d(TAG,"GetAllRanksByTour success");


                tv.setHeaderAdapter(new SimpleTableHeaderAdapter(RankActivity.this,"Club","Games","Wons","Draws","Losts","Goals","Concedes","Points"));
                tv.setDataAdapter(new RankDataAdapter(RankActivity.this,ranks));
                //tv.setDataAdapter(new SimpleTableDataAdapter(RoundDetail.this,matchAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"GetAllMatchesByRound failed "+error.getMessage());
                Toast.makeText(RankActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });


    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
