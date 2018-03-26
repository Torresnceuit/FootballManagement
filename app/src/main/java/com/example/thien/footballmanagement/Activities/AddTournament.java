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
/**
 * AddTournament Activity
 */

public class AddTournament extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private Button btnSave;
    private EditText mTourName;
    private RestTourService restTourService;
    private Tournament mTour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);
        // intitialize members
        btnSave = (Button) findViewById(R.id.btn_saveTour);
        mTourName = (EditText) findViewById(R.id.addtour_name);
        restTourService = new RestTourService();
        // set click event
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }
    // save tournament
    private void save(){
        // get intent
        Intent i = getIntent();
        mTour.Name = mTourName.getText().toString();
        // get league_Id pass in intent
        mTour.LeagueId = i.getStringExtra("league_Id");
        // check form validation before save
        if(!validate()){
            onSaveFailed();
            return;
        }
        // call tournament service interface to update a tour
        restTourService.getService().updateTour(mTour, new Callback<Tournament>() {

            @Override
            public void success(Tournament tournament, Response response) {
                Log.d(TAG,"Add Tournament Successfully!");
                onSaveSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                onSaveFailed();
            }
        });
    }
    // TO DO when save failed
    private void onSaveFailed(){
        Toast.makeText(getBaseContext(), "Add Tournament failed", Toast.LENGTH_LONG).show();

        btnSave.setEnabled(true);
    }
    // TO DO when save success
    private void onSaveSuccess(){
        btnSave.setEnabled(false);
        Toast.makeText(getBaseContext(), "Add Tournament done successfully!", Toast.LENGTH_LONG).show();
        finish();
    }
    // validation check
    public boolean validate() {
        boolean valid = true;

        String tourName = mTourName.getText().toString();
        if(tourName.isEmpty()){
            // set error
            mTourName.setError("Name can not be empty, please fill in club name!");
            valid = false;
        }else {
            mTourName.setError(null);
        }

        return valid;
    }
}
