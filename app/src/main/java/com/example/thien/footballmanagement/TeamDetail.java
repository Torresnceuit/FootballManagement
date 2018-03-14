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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TeamDetail extends AppCompatActivity {

    private List<Player> players; // Store all players retrieve from response

    private String TAG = "TeamDetailActivity";
    public static final String MYPREF = "com.example.thien";
    public SharedPreferences sharedPreferences;
    public static String bearer = "";
    public static Context contextOfApplication;

    GridView gridView;
    Button btnAdd;
    Button btnSave;
    RestTeamService restTeamService;
    RestPlayerService restPlayerService;
    TextView player_Id;

    ImageView teamDetailLogo;
    EditText teamDetailName;
    Team _team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        // Initiate
        restPlayerService = new RestPlayerService();
        restTeamService = new RestTeamService();
        contextOfApplication = getApplicationContext();
        _team = new Team();
        teamDetailLogo = (ImageView) findViewById(R.id.detailTeamLogo);
        teamDetailName = (EditText) findViewById(R.id.detailTeamName);
        btnAdd= (Button) findViewById(R.id.btnAddPlayer);
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
        // Get bearer from SharePreferences
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

    ////Open AddPlayer

    private void openAdd(){
        /*Intent i = new Intent(TournamentDetail.this,AddTeam.class);
        i.putExtra("tour_Id",_tour.Id);
        startActivity(i);*/
    }

    //  Save Team after Edited
    private void save(){
        _team.Name = teamDetailName.getText().toString();
        restTeamService.getService().updateTeam(bearer, _team, new Callback<Team>() {


            @Override
            public void success(Team team, Response response) {
                Log.d(TAG,"Update Team Successfully!");
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

        //Call to server to grab list of players records. this is a asyn
        String team_Id="";
        Intent i = getIntent();
        team_Id = i.getStringExtra("team_Id");
        Log.d(TAG,"team_Id= "+team_Id);
        // Get team info
        restTeamService.getService().getTeamById(bearer, team_Id, new Callback<Team>() {


            @Override
            public void success(Team team, Response response) {
                Log.d(TAG, "fetch Team successfully!");
                _team = team;
                if(team.Logo!=null){
                    String teamLogoUrl = team.Logo.replaceAll("localhost","10.0.2.2");

                    // Get Image data from Url and pass to teamDetailLogo
                    Picasso.with(getApplicationContext())
                            .load(teamLogoUrl)
                            .resize(200,200)
                            .centerCrop()
                            .into(teamDetailLogo);
                }else {
                    teamDetailLogo.setImageResource(R.drawable.club); // Set default logo
                }

                teamDetailName.setText(team.Name); // Set team name
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"fetch Team failed!");
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        /// Get all players by team Id and display in a gridview
        restPlayerService.getService().getAllPlayersByTeam(bearer, team_Id, new Callback<List<Player>>() {
            @Override
            public void success(List<Player> players, Response response) {
                GridView gvPlayer = (GridView) findViewById(R.id.gridViewPlayer);

                Log.d(TAG,"GetAllPlayersByTeam success");

                PlayerAdapter playerAdapter = new PlayerAdapter(TeamDetail.this, R.layout.view_player_entry, players);

                gvPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //set gridview click event
                        player_Id = (TextView) view.findViewById(R.id.player_Id);
                        String playerId = player_Id.getText().toString();
                        /*Intent objIndent = new Intent(getApplicationContext(), PlayerDetail.class);
                        objIndent.putExtra("player_Id", playerId);
                        startActivity(objIndent);*/

                    }
                });
                gvPlayer.setAdapter(playerAdapter); // set gridview adpater
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"GetAllPlayersByTeam failed");
                Toast.makeText(TeamDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });



    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
