package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RoundDetail extends AppCompatActivity {

    // Store all matches for further uses here
    public static List<Match> mMatches;
    private final String TAG = this.getClass().getSimpleName();
    public static Context contextOfApplication;
    ListView listView;
    Button btnProc;
    RestRoundService restRoundService;
    RestMatchService restMatchService;
    TextView match_Id, roundDetailName;
    Round mRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_detail);

        // Initiate
        restRoundService = new RestRoundService();
        restMatchService = new RestMatchService();
        mRound = new Round();
        mMatches = new ArrayList<Match>();
        contextOfApplication = getApplicationContext();
        roundDetailName = (TextView) findViewById(R.id.detailRoundName);
        btnProc= (Button) findViewById(R.id.btn_proceed);
        btnProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        refreshScreen();
    }
    /// Get random interger
    private int  getRandom(int min, int max){
        Random rn = new Random();
        int value = rn.nextInt(max-min+1)+min;

        return value;

    }


    // Simulate matches by random scores
    private void proceed(){
        int maxRand = getRandom(0,5);
        Log.d(TAG, "Before Proceed matches, _matches size = "+mMatches.size());
        for(int i = 0;i< mMatches.size();i++){
            if(mMatches.get(i).HomeId!=null && mMatches.get(i).AwayId!=null){
                mMatches.get(i).HomeScore = getRandom(0,maxRand)+1;
                mMatches.get(i).AwayScore = getRandom(0,maxRand);
                mMatches.get(i).IsDone = true;
            }

        }
        mRound.IsDone = true;

        // disable proceed button after proceed done
        btnProc.setEnabled(false);
        btnProc.setBackgroundColor(Color.BLACK);
        restMatchService.getService().proceedMatches(mMatches, new Callback<Object>() {

            @Override
            public void success(Object o, Response response) {
                Log.d(TAG,"Proceed done!");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,error.getMessage());
            }
        });
        Log.d(TAG,"Proceed finished!");
        Toast.makeText(getApplicationContext(),"Matches done!",Toast.LENGTH_LONG).show();
        restRoundService.getService().updateRound(mRound, new Callback<Round>() {
            @Override
            public void success(Round round, Response response) {

                recreate(); // reload the screen
                Toast.makeText(getApplicationContext(),"Proceed Done!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"Update round failed!");
            }
        });

    }

    // TO DO: when refresh screen
    private void refreshScreen() {

        //Call to server to grab list of matches records. this is a asyn
        String round_Id="";
        Intent i = getIntent();
        round_Id = i.getStringExtra("round_Id");
        Log.d(TAG,"round_Id= "+round_Id);
        // Get round info
        restRoundService.getService().getRoundById(round_Id, new Callback<Round>() {
            @Override
            public void success(Round round, Response response) {
                Log.d(TAG, "fetch Round successfully!");
                mRound = round;
                roundDetailName.setText("Round "+mRound.Name);
                // IF: round is done, set background to black
                if(mRound.IsDone){
                    btnProc.setEnabled(false);
                    btnProc.setBackgroundColor(Color.BLACK);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"fetch Round failed!");
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        /// Get all matches by round Id
        restMatchService.getService().getAllMatchesByRound(round_Id, new Callback<List<Match>>() {
            @Override
            public void success(List<Match> matches, Response response) {
                //ListView lv = (ListView) findViewById(R.id.listView);
                TableView<Match> tv = (TableView<Match>) findViewById(R.id.tableView);

                // Store all matches in _matches from response body
                for(Match match : matches){
                    mMatches.add(match);
                }

                Log.d(TAG,"GetAllMatchesByRound success _matches size = "+mMatches.size());
                // Set Table Header
                tv.setHeaderAdapter(new SimpleTableHeaderAdapter(RoundDetail.this,"Home","","","Away"));
                // Set Match Data Adapter to the Table View
                tv.setDataAdapter(new MatchDataAdapter(RoundDetail.this,matches));


            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"GetAllMatchesByRound failed "+error.getMessage());
                Toast.makeText(RoundDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });


    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
