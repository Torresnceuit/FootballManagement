package com.example.thien.footballmanagement;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by thien on 11/03/2018.
 */

public class MatchDataAdapter extends TableDataAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private RestTeamService restTeamService;
    private static final int TEXT_SIZE = 14;

    public MatchDataAdapter(Context context, List<Match> matches) {
        super(context, matches);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Match match = (Match) getRowData(rowIndex);

        View renderedView = null;
        restTeamService = new RestTeamService();

        switch (columnIndex) {
            case 0:
                renderedView = renderHomeName(match);
                break;
            case 1:
                renderedView = renderHomeScore(match);
                break;
            case 2:
                renderedView = renderAwayScore(match);
                break;
            case 3:
                renderedView = renderAwayName(match);
                break;
        }

        return renderedView;
    }
    // render name of home team
    private View renderHomeName(Match match){

        final TextView tv = new TextView(getContext());
        // call team service to get a team
        restTeamService.getService().getTeamById(match.HomeId, new Callback<Team>() {
            @Override
            public void success(Team team, Response response) {

                tv.setText(team.Name);
                tv.setPadding(20, 10, 20, 10);
                tv.setTextSize(TEXT_SIZE);
                tv.setGravity(Gravity.CENTER);
                Log.d(TAG,"get Home Team successfully");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"get Home Team Failed "+error.getMessage());

            }
        });
        return tv;
    }
    // render score of home team
    private View renderHomeScore(Match match){

        final TextView textView = new TextView(getContext());
        textView.setText(match.HomeScore+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render score of away team
    private View renderAwayScore(Match match){

        final TextView textView = new TextView(getContext());
        textView.setText(match.AwayScore+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render name of away team
    private View renderAwayName(Match match){

        final TextView tv = new TextView(getContext());
        // get team by Id to get a name
        restTeamService.getService().getTeamById(match.AwayId, new Callback<Team>() {
            @Override
            public void success(Team team, Response response) {

                tv.setText(team.Name);
                tv.setPadding(20, 10, 20, 10);
                tv.setTextSize(TEXT_SIZE);
                //tv.setSingleLine();
                Log.d(TAG,"get Away Team successfully");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"get Away Team Failed "+error.getMessage());

            }
        });
        
        return tv;
    }


}
