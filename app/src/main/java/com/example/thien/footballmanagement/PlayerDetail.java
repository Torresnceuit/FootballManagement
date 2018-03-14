package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import retrofit.converter.Converter;

public class PlayerDetail extends AppCompatActivity {

    private String TAG = "PlayerDetailActivity";
    public static final String MYPREF = "com.example.thien";
    public SharedPreferences sharedPreferences;
    public static String bearer = "";
    public static Context contextOfApplication;

    RestPlayerService restPlayerService;

    ImageView playerDetailAvatar;
    EditText playerDetailName;
    EditText playerDetailAge;
    EditText playerDetailNumber;
    EditText playerDetailNationality;
    EditText playerDetailPos;

    Button btnSave;

    Player _player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        restPlayerService = new RestPlayerService();
        contextOfApplication = getApplicationContext();
        _player = new Player();
        playerDetailAvatar = (ImageView) findViewById(R.id.detailPlayerAvatar);
        playerDetailName = (EditText) findViewById(R.id.detailPlayerName);
        playerDetailAge = (EditText) findViewById(R.id.detailPlayerAge);
        playerDetailNumber = (EditText) findViewById(R.id.detailPlayerNumber);
        playerDetailNationality = (EditText) findViewById(R.id.detailPlayerNation);
        playerDetailPos = (EditText) findViewById(R.id.detailPlayerPos) ;
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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

    private void save() {
        _player.Name = playerDetailName.getText().toString();
        _player.Age = Integer.parseInt(playerDetailAge.getText().toString());
        _player.Nationality = playerDetailNationality.getText().toString();
        _player.Number = Integer.parseInt(playerDetailNumber.getText().toString());
        _player.Position = playerDetailPos.getText().toString().split(",");
        restPlayerService.getService().updatePlayer(bearer, _player, new Callback<Player>() {


            @Override
            public void success(Player player, Response response) {
                Log.d(TAG,"Update Player Successfully!");
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

        //Call to server to grab list of student records. this is a asyn
        String player_Id="";
        Intent i = getIntent();
        player_Id = i.getStringExtra("player_Id");
        Log.d(TAG,"player_Id= "+player_Id);
        //////GET LEAGUE INFO
        restPlayerService.getService().getPlayerById(bearer, player_Id, new Callback<Player>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void success(Player player, Response response) {
                Log.d(TAG, "fetch player successfully!");
                _player = player;
                if(player.Avatar!=null){
                    String playerLogoUrl = player.Avatar.replaceAll("localhost","10.0.2.2");
                    Picasso.with(getApplicationContext())
                            .load(playerLogoUrl)
                            .resize(200,200)
                            .centerCrop()
                            .into(playerDetailAvatar);
                }else {
                    playerDetailAvatar.setImageResource(R.drawable.player);
                }

                playerDetailName.setText(player.Name);
                playerDetailAge.setText(player.Age+"");
                playerDetailNumber.setText(player.Number+"");
                playerDetailNationality.setText(player.Nationality);
                playerDetailPos.setText(String.join(",",player.Position));



            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"fetch player failed!");
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
