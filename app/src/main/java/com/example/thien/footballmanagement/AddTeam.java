package com.example.thien.footballmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddTeam extends AppCompatActivity {

    private String TAG = "AddTeamActivity";
    public static final String MYPREF = "com.example.thien";
    public SharedPreferences sharedPreferences;
    public static String bearer = "";

    private Button btnSave;
    private EditText _teamName;
    private RestTeamService restTeamService;
    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        btnSave = (Button) findViewById(R.id.btn_saveTeam);
        _teamName = (EditText) findViewById(R.id.addteam_name);
        restTeamService = new RestTeamService();
        sharedPreferences = getSharedPreferences(MYPREF,MODE_PRIVATE);
        bearer = "Bearer "+sharedPreferences.getString("access_token","");
        team = new Team();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save(){
        Intent i = getIntent();
        team.Name = _teamName.getText().toString();
        team.TourId = i.getStringExtra("tour_Id");

        if(!validate()){
            onSaveFailed();
            return;
        }
        restTeamService.getService().updateTeam(bearer, team, new Callback<Team>() {



            @Override
            public void success(Team team, Response response) {
                Log.d(TAG,"Add Team Successfully!");
                onSaveSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                onSaveFailed();
            }
        });
    }

    private void onSaveFailed(){
        Toast.makeText(getBaseContext(), "Add Team failed", Toast.LENGTH_LONG).show();

        btnSave.setEnabled(true);
    }

    private void onSaveSuccess(){
        btnSave.setEnabled(false);
        Toast.makeText(getBaseContext(), "Add Tournament done successfully!", Toast.LENGTH_LONG).show();
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String teamName = _teamName.getText().toString();
        if(teamName.isEmpty()){
            _teamName.setError("Name can not be empty, please fill in club name!");
            valid = false;
        }else {
            _teamName.setError(null);
        }

        return valid;
    }
}
