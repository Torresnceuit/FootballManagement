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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by thien on 10/03/2018.
 */

public class MatchAdapter extends ArrayAdapter<Match> {

    private final String TAG = this.getClass().getSimpleName();
    private RestTeamService restTeamService;
    private Team mHomeTeam;
    private Team mAwayTeam;

    public MatchAdapter(@NonNull Context context, int resource, @NonNull List<Match> matches) {
        super(context, resource, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        restTeamService = new RestTeamService();
        mHomeTeam = new Team();
        mAwayTeam = new Team();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_match_entry, parent, false);
        }

        final Match match = getItem(position);

        if (match != null) {
            TextView tvMatchId = (TextView) v.findViewById(R.id.match_Id);
            final TextView tvHomeName = (TextView) v.findViewById(R.id.homeName);
            TextView tvHomeScore = (TextView) v.findViewById(R.id.homeScore);
            final TextView tvAwayName = (TextView) v.findViewById(R.id.awayName);
            TextView tvAwayScore = (TextView) v.findViewById(R.id.awayScore);
            tvMatchId.setText( match.Id);
            tvMatchId.setGravity(Gravity.CENTER);
            restTeamService.getService().getTeamById(match.HomeId, new Callback<Team>() {
                @Override
                public void success(Team team, Response response) {
                    mHomeTeam = team;
                    tvHomeName.setText(team.Name);
                    tvHomeName.setGravity(Gravity.CENTER);
                    Log.d(TAG,"get Home Team successfully");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG,"get Home Team Failed "+error.getMessage());

                }
            });

            restTeamService.getService().getTeamById(match.AwayId, new Callback<Team>() {
                @Override
                public void success(Team team, Response response) {
                    mAwayTeam = team;
                    tvAwayName.setText(team.Name);
                    tvAwayName.setGravity(Gravity.CENTER);
                    Log.d(TAG,"get Away Team successfully");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG,"get Away Team Failed "+error.getMessage());
                }
            });


            tvHomeScore.setText(""+match.HomeScore);
            tvHomeScore.setGravity(Gravity.CENTER);

            tvAwayScore.setText(""+match.AwayScore);
            tvAwayScore.setGravity(Gravity.CENTER);
        }

        return v;
    }
}
