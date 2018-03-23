package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by thien on 7/03/2018.
 * League Adapter is used to render custom view
 * For Listview to display custom data
 */

public class LeagueAdapter extends ArrayAdapter<League> {

    private final String TAG = this.getClass().getSimpleName();
    private RestLeagueService restLeagueService;

    // Constructor
    public LeagueAdapter(Context context, int resource, List<League> league) {
        super(context, resource, league);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        restLeagueService = new RestLeagueService();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_league_entry, parent, false); // Get view from view_league_entry layout

        }
        // Get a single league at position
        final League league = getItem(position);


        if (league != null) {
            TextView tvLeagueId = (TextView) v.findViewById(R.id.league_Id);
            TextView tvLeagueName = (TextView) v.findViewById(R.id.league_name);
            ImageView ivLeagueLogo = (ImageView) v.findViewById(R.id.league_logo);
            tvLeagueId.setText( league.Id);
            // League Id display in the center
            tvLeagueId.setGravity(Gravity.CENTER);
            tvLeagueName.setText(league.Name);
            // League Name display in the center
            tvLeagueName.setGravity(Gravity.CENTER); 
            //Build Url to get image
            if(league.Logo!=null){
                String leagueLogoUrl = league.Logo
                        .replaceAll("localhost","10.0.2.2");
                Log.d(TAG,leagueLogoUrl);

                // Use Picasso for getting image from leagueLogoUrl and pass image to ivLeagueLogo
                Picasso.with(this.getContext())
                        .load(leagueLogoUrl)
                        .resize(50,50)
                        .centerCrop()
                        .into(ivLeagueLogo);
            }else {
                // Use the default image
                ivLeagueLogo.setImageResource(R.drawable.club); 
            }

        }
        final Button btnDel = (Button) v.findViewById(R.id.btnDelLeague);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(league);

            }
        });
        return v;
    }


    public void delete(final League league){
        // Call to LeagueService -> deleteLeague, Get a list of League in response
        restLeagueService.getService().deleteLeague(league.Id, new Callback<List<League>>() {
            @Override
            public void success(List<League> leagues, Response response) {
                Log.d(TAG,"Delete league done successfully!");
                remove(league);
                // notify changed to reload
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"Delete failed!");
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
