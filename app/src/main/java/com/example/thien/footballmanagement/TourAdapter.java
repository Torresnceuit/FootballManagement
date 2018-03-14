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
 * Created by thien on 8/03/2018.
 * TourAdapter extends from ArrayAdapter
 * Used to load tour into a custom view
 */

public class TourAdapter extends ArrayAdapter<Tournament> {

    private final String TAG = "TourAdapter";
    public static final String MYPREF = "com.example.thien";
    public static String bearer = "";
    private RestTourService restTourService;
    private Context applicationContext;

    public TourAdapter(Context context, int resource, List<Tournament> tours) {
        super(context, resource, tours);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        restTourService = new RestTourService();

        applicationContext = LeagueDetail.getContextOfApplication(); // Get LeagueDetail Context to use SharePreference

        bearer = "Bearer "+applicationContext
                .getSharedPreferences(MYPREF,Context.MODE_PRIVATE)
                .getString("access_token",""); // Build bearer for authentication
        Log.d(TAG,bearer);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_tour_entry, parent, false);
        }

        final Tournament tour = getItem(position); // Get tour at position

        if (tour != null) {
            TextView tvTourId = (TextView) v.findViewById(R.id.tour_Id);
            TextView tvTourName = (TextView) v.findViewById(R.id.tour_name);
            ImageView ivTourLogo = (ImageView) v.findViewById(R.id.tour_logo);
            tvTourId.setText( tour.Id);
            tvTourId.setGravity(Gravity.CENTER);
            tvTourName.setText(tour.Name);
            tvTourName.setGravity(Gravity.CENTER);
            if(tour.Logo!=null){
                // Build Tour Logo Url
                String tourLogoUrl = tour.Logo.replaceAll("localhost","10.0.2.2");
                Log.d(TAG,tourLogoUrl);

                // Get Image from Url and pass data to ivTourLogo
                Picasso.with(this.getContext())
                        .load(tourLogoUrl)
                        .resize(50,50)
                        .centerCrop()
                        .into(ivTourLogo);

            }else {
                ivTourLogo.setImageResource(R.drawable.cup); // Use default image for Tour Logo
            }




        }

        final Button btnDel = (Button) v.findViewById(R.id.btnDelTour);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(tour);

            }
        });

        return v;
    }

    /*
        delete a tournament, parameter: Tournament
     */
    private void delete(final Tournament tour){
        restTourService.getService().deleteTour(bearer, tour.Id, new Callback<List<Tournament>>() {
            @Override
            public void success(List<Tournament> tournaments, Response response) {
                Log.d(TAG,"Delete tour done successfully!");
                remove(tour);
                notifyDataSetChanged(); // Notify to reload data set
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"Delete tour failed!");
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
