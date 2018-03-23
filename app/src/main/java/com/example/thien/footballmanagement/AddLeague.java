package com.example.thien.footballmanagement;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
/**
 * AddLeague Activity
 */

public class AddLeague extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private Button btnSave;
    private EditText mLeagueName;
    private RestLeagueService restLeagueService;
    private League mLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_league);
        // initiate members
        btnSave = (Button) findViewById(R.id.btn_saveLeague);
        mLeagueName = (EditText) findViewById(R.id.addleague_name);
        restLeagueService = new RestLeagueService();
        mLeague = new League();
        // set function save() to click event
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    // save a league
    private void save(){
        mLeague.Name = mLeagueName.getText().toString();
        // check form validate or not
        if(!validate()){
            onSaveFailed();
            return;
        }
        // call league service to update
        restLeagueService.getService().updateLeague(mLeague, new Callback<League>() {
            @Override
            public void success(League league, Response response) {
                Log.d(TAG,"Add League Successfully!");
                onSaveSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                onSaveFailed();
            }
        });
    }
    // save failed
    private void onSaveFailed(){
        Toast.makeText(getBaseContext(), "Add League failed", Toast.LENGTH_LONG).show();

        btnSave.setEnabled(true);
    }
    // save successfully
    private void onSaveSuccess(){
        btnSave.setEnabled(false);
        Toast.makeText(getBaseContext(), "Add League done successfully!", Toast.LENGTH_LONG).show();
        finish();
    }
    // check validation
    public boolean validate() {
        boolean valid = true;

        String leagueName = mLeagueName.getText().toString();
        if(leagueName.isEmpty()){
            mLeagueName.setError("Name can not be empty, please fill in club name!");
            valid = false;
        }else {
            mLeagueName.setError(null);
        }

        return valid;
    }
}
