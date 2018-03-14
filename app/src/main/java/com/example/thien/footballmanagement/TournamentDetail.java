package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TournamentDetail extends AppCompatActivity {

    private List<Team> teams; // Store teams from http response
    private List<Round> rounds; // Store rounds from http response
    private String TAG = "TournamentDetailActivity";
    public static final String MYPREF = "com.example.thien";
    public SharedPreferences sharedPreferences;
    public static String bearer = "";
    public static Context contextOfApplication; // Instance of TournamentDetail context

    GridView gridView;
    Button btnAdd;
    Button btnSave;
    Button btnShow;
    Button btnGenerate;
    RestTeamService restTeamService;
    RestTourService restTourService;
    RestRoundService restRoundService;

    TextView team_Id;
    TextView round_Id;
    ImageView tourDetailLogo;
    EditText tourDetailName;
    Tournament _tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_detail);

        restTourService = new RestTourService();
        restTeamService = new RestTeamService();
        restRoundService = new RestRoundService();
        contextOfApplication = getApplicationContext();
        _tour = new Tournament();
        tourDetailLogo = (ImageView) findViewById(R.id.detailTourLogo);
        tourDetailName = (EditText) findViewById(R.id.detailTourName);
        btnAdd= (Button) findViewById(R.id.btnAddTeam);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd();
            }
        });
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRank();
            }
        });
        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateFixture();
            }
        });
        //btnAdd.setOnClickListener(this);
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

    // Open AddTeam Activity
    private void openAdd(){
        Intent i = new Intent(TournamentDetail.this,AddTeam.class);
        i.putExtra("tour_Id",_tour.Id); // Pass tour_Id in Intent
        startActivity(i);
    }

    // Generate the rank of tournament
    private void showRank(){
        //Collect rank first
        restTourService.getService().generateRank(bearer, _tour.Id, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                // Display rank
                Intent i = new Intent(TournamentDetail.this,RankActivity.class);
                i.putExtra("tour_Id",_tour.Id);
                startActivity(i);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),"Generate Rank Failed!",Toast.LENGTH_LONG).show();
            }
        });


    }

    // Generate a new fixture for a new season
    private void generateFixture(){
        Intent i = getIntent();
        String tour_Id = i.getStringExtra("tour_Id");
        restTourService.getService().generateFixture(bearer, tour_Id, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d(TAG,"Generate Fixture Successfully!");
                recreate();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"Generate Fixture Failed! "+error.getMessage());
            }
        });
    }

    // Save tournament after editing
    private void save(){
        _tour.Name = tourDetailName.getText().toString();
        restTourService.getService().updateTour(bearer, _tour, new Callback<Tournament>() {


            @Override
            public void success(Tournament tournament, Response response) {
                Log.d(TAG,"Update Tournament Successfully!");
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    private void refreshScreen() {

        //Call to server to grab list of teams and rounds records. this is a asyn
        String tour_Id="";
        Intent i = getIntent();
        tour_Id = i.getStringExtra("tour_Id");
        Log.d(TAG,"tour_Id= "+tour_Id);
        // Get Tournament Info first
        restTourService.getService().getTourById(bearer, tour_Id, new Callback<Tournament>() {


            @Override
            public void success(Tournament tournament, Response response) {
                Log.d(TAG, "fetch Tour successfully!");
                _tour = tournament;
                if(tournament.Logo!=null){
                    String tourLogoUrl = tournament.Logo.replaceAll("localhost","10.0.2.2");

                    // Display Tournament Logo
                    Picasso.with(getApplicationContext())
                            .load(tourLogoUrl)
                            .resize(200,200)
                            .centerCrop()
                            .into(tourDetailLogo);
                }else {
                    tourDetailLogo.setImageResource(R.drawable.cup); // Set default tournament logo
                }

                tourDetailName.setText(tournament.Name); // Get Tournament name
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"fetch Tour failed!");
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        /// Get all teams by tournament Id
        restTeamService.getService().getAllTeamsByTour(bearer, tour_Id, new Callback<List<Team>>() {
            @Override
            public void success(List<Team> teams, Response response) {
                GridView gvTeam = (GridView) findViewById(R.id.gridViewTeam);

                Log.d(TAG,"GetAllTeamsByTour success");

                TeamAdapter teamAdapter = new TeamAdapter(TournamentDetail.this, R.layout.view_team_entry, teams);

                gvTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // set click event
                        team_Id = (TextView) view.findViewById(R.id.team_Id);
                        String teamId = team_Id.getText().toString();
                        // Open TeamDetail Activity: show all players
                        Intent objIndent = new Intent(getApplicationContext(), TeamDetail.class);
                        objIndent.putExtra("team_Id", teamId);
                        startActivity(objIndent);

                    }
                });
                gvTeam.setAdapter(teamAdapter); //Set gridview adapter = teamAdapter
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"GetAllTeamsByTour failed");
                Toast.makeText(TournamentDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        // Get all rounds by tournament Id
        restRoundService.getService().getAllRoundsByTour(bearer, tour_Id, new Callback<List<Round>>() {
            @Override
            public void success(List<Round> rounds, Response response) {
                GridView gvRound = (GridView) findViewById(R.id.gridViewRound);

                Log.d(TAG,"GetAllRoundsByTour success");

                RoundAdapter roundAdapter = new RoundAdapter(TournamentDetail.this, R.layout.view_round_entry, rounds);

                gvRound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        round_Id = (TextView) view.findViewById(R.id.round_Id);
                        String roundId = round_Id.getText().toString();
                        // Open RoundDetail Activity: show all matches
                        Intent objIndent = new Intent(getApplicationContext(), RoundDetail.class);
                        objIndent.putExtra("round_Id", roundId);
                        startActivity(objIndent);

                    }
                });
                gvRound.setAdapter(roundAdapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
