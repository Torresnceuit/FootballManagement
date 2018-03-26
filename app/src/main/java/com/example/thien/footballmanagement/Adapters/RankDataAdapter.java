package com.example.thien.footballmanagement;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by thien on 11/03/2018.
 */

public class RankDataAdapter extends TableDataAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private RestTeamService restTeamService;
    private static final int TEXT_SIZE = 14;

    public RankDataAdapter(Context context, List<Rank> ranks) {
        super(context, ranks);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Rank rank = (Rank) getRowData(rowIndex);

        View renderedView = null;
        restTeamService = new RestTeamService();

        switch (columnIndex) {
            case 0:
                renderedView = renderTeamName(rank);
                break;
            case 1:
                renderedView = renderGames(rank);
                break;
            case 2:
                renderedView = renderWons(rank);
                break;
            case 3:
                renderedView = renderLosts(rank);
                break;
            case 4:
                renderedView = renderDraws(rank);
                break;
            case 5:
                renderedView = renderGoals(rank);
                break;
            case 6:
                renderedView = renderConcedes(rank);
                break;
            case 7:
                renderedView = renderPoints(rank);
                break;
        }

        return renderedView;
    }
    // render points
    private View renderPoints(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Points+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }
    // render concedes
    private View renderConcedes(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Concede+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }
    // render goals
    private View renderGoals(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Goals+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render draws
    private View renderDraws(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Draw+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render losts
    private View renderLosts(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Lost+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render wons
    private View renderWons(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Won+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render games
    private View renderGames(Rank rank) {
        final TextView textView = new TextView(getContext());
        textView.setText(rank.Games+"");
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    // render team name
    private View renderTeamName(Rank rank) {
        final TextView tv = new TextView(getContext());

        restTeamService.getService().getTeamById(rank.TeamId, new Callback<Team>() {
            @Override
            public void success(Team team, Response response) {

                tv.setText(team.Name);
                tv.setPadding(20, 10, 20, 10);
                tv.setTextSize(TEXT_SIZE);
                tv.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
                tv.setGravity(Gravity.CENTER);
                Log.d(TAG,"get Team successfully");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"get Team Failed "+error.getMessage());

            }
        });

        return tv;
    }
}
