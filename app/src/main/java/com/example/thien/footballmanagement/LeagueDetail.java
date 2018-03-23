package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteCallbackList;
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

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LeagueDetail extends AppCompatActivity {

    // store tours from response to the list
    private List<Tournament> mTours;
    private final String TAG = this.getClass().getSimpleName();
    //instance of LeagueDetail context
    public static Context contextOfApplication;

    ListView listView;
    Button btnAdd, btnSave;
    RestTourService restTourService;
    RestLeagueService restLeagueService;
    TextView tour_Id;
    ImageView leagueDetailLogo;
    EditText leagueDetailName;
    League mLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_detail);
        // initialize members
        restTourService = new RestTourService();
        restLeagueService = new RestLeagueService();
        contextOfApplication = getApplicationContext();
        mLeague = new League();
        leagueDetailLogo = (ImageView) findViewById(R.id.detailLeagueLogo);
        leagueDetailName = (EditText) findViewById(R.id.detailLeagueName);
        btnAdd= (Button) findViewById(R.id.btnAddTour);
        // set click event of button Add
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd();
            }
        });
        btnSave = (Button) findViewById(R.id.btn_save);
        // set click event of button Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();
        // refresh the screen
        refreshScreen();
    }

    // Open Add Screen
    private void openAdd(){
        Intent i = new Intent(LeagueDetail.this,AddTournament.class);
        i.putExtra("league_Id",mLeague.Id);
        startActivity(i);
    }

    // Save League After Edit
    private void save(){
        mLeague.Name = leagueDetailName.getText().toString();
        restLeagueService.getService().updateLeague(mLeague, new Callback<League>() {
            @Override
            public void success(League league, Response response) {
                Log.d(TAG,"Update League Successfully!");
                finish();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    // TO DO when refresh
    private void refreshScreen() {

        //Call to server to grab list of tournaments records. this is an asyn
        String league_Id="";
        Intent i = getIntent();
        league_Id = i.getStringExtra("league_Id"); // Get league_Id passed through Intent
        Log.d(TAG,"league_Id= "+league_Id);
        // Get league infor first
        restLeagueService.getService().getLeagueById(league_Id, new Callback<League>() {
            @Override
            public void success(League league, Response response) {
                Log.d(TAG, "fetch League successfully!");
                mLeague = league;
                if(league.Logo!=null){
                    //Build Image Url
                    String leagueLogoUrl = league.Logo
                            .replaceAll("localhost","10.0.2.2"); 

                    // Load Image from Url to leagueDetailLogo
                    Picasso.with(getApplicationContext())
                            .load(leagueLogoUrl)
                            .resize(200,200)
                            .centerCrop()
                            .into(leagueDetailLogo);
                }else {
                    // Set default Logo
                    leagueDetailLogo.setImageResource(R.drawable.club); 
                }
                // set league name
                leagueDetailName.setText(league.Name);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"fetch League failed!");
                if(error.getMessage().length()>0){
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
        /// Get all tournaments from league Id
        restTourService.getService().getAllToursByLeague(league_Id, new Callback<List<Tournament>>() {
            @Override
            public void success(List<Tournament> tournaments, Response response) {
                ListView lv = (ListView) findViewById(R.id.listView);

                Log.d(TAG,"GetAllToursByLeague success");

                // Set tour adapter to display on listview
                TourAdapter tourAdapter = new TourAdapter(LeagueDetail.this, R.layout.view_tour_entry, tournaments);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // set Onclick Event
                        tour_Id = (TextView) view.findViewById(R.id.tour_Id);
                        String tourId = tour_Id.getText().toString();

                        // Open TournamentDetail Activity and pass tour_Id in Intent
                        Intent objIndent = new Intent(getApplicationContext(), TournamentDetail.class);
                        objIndent.putExtra("tour_Id", tourId);
                        startActivity(objIndent);

                    }
                });
                // set listview adapter
                lv.setAdapter(tourAdapter); 
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"GetAllToursByLeague failed");
                if(error.getMessage().length()>0){
                    Toast.makeText(LeagueDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
