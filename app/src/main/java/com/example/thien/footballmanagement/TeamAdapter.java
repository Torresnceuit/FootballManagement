package com.example.thien.footballmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by thien on 9/03/2018.
 * TeamAdapter build the view before display in
 * Gridview or Listview
 */

public class TeamAdapter extends ArrayAdapter<Team> {

    private final String TAG = this.getClass().getSimpleName();
    private RestTeamService restTeamService;

    public TeamAdapter(@NonNull Context context, int resource, List<Team> teams) {
        super(context, resource, teams);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

         restTeamService = new RestTeamService();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_team_entry, parent, false);
        }

        final Team team = getItem(position);

        if (team != null) {
            TextView tvTeamId = (TextView) v.findViewById(R.id.team_Id);
            TextView tvTeamName = (TextView) v.findViewById(R.id.team_name);
            ImageView ivTeamLogo = (ImageView) v.findViewById(R.id.team_logo);
            tvTeamId.setText( team.Id);
            tvTeamId.setGravity(Gravity.CENTER);
            tvTeamName.setText(team.Name);
            tvTeamName.setGravity(Gravity.CENTER);
            if(team.Logo!=null){
                String teamLogoUrl = team.Logo.replaceAll("localhost","10.0.2.2");
                Log.d(TAG,teamLogoUrl);

                // Retrieve image data for Team Logo and pass to ivTeamLogo
                Picasso.with(this.getContext())
                        .load(teamLogoUrl)
                        .resize(50,50)
                        .centerCrop()
                        .into(ivTeamLogo);

            }else {
                // Set default team logo
                ivTeamLogo.setImageResource(R.drawable.club); 
            }




        }

        final Button btnDel = (Button) v.findViewById(R.id.btnDelTeam);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(team);

            }
        });

        return v;
    }

    // Delete a team: para {team}
    private void delete(final Team team){
        restTeamService.getService().deleteTeam(team.Id, new Callback<List<Team>>() {


            @Override
            public void success(List<Team> teams, Response response) {
                Log.d(TAG,"Delete team done successfully!");
                remove(team);
                notifyDataSetChanged(); // Notify changed on data
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"Delete team failed!");
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
