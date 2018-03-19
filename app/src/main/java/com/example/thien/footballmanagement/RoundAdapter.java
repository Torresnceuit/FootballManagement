package com.example.thien.footballmanagement;

import android.content.Context;
import android.graphics.Color;
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
 * RoundAdapter extends ArrayAdapter
 * Build Custom View from array of round
 * Before Display on GridView or Listview
 */

public class RoundAdapter extends ArrayAdapter<Round> {

    private final String TAG = this.getClass().getSimpleName();
    private RestRoundService restRoundService;
    private Context applicationContext;

    public RoundAdapter(@NonNull Context context, int resource, @NonNull List<Round> rounds) {
        super(context, resource, rounds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        restRoundService = new RestRoundService();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_round_entry, parent, false);
        }

        final Round round = getItem(position);

        if (round != null) {
            TextView tvRoundId = (TextView) v.findViewById(R.id.round_Id);
            TextView tvRoundName = (TextView) v.findViewById(R.id.round_name);

            tvRoundId.setText( round.Id);
            tvRoundId.setGravity(Gravity.CENTER);
            tvRoundName.setText("R"+round.Name);
            tvRoundName.setGravity(Gravity.CENTER);
            //IF: round is done, set background color to black
            if(round.IsDone){
                tvRoundName.setBackgroundColor(Color.BLACK);
            }

        }



        return v;
    }


}
