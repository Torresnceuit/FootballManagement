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
 * Created by thien on 10/03/2018.
 * PlayerAdapter extends from ArrayAdapter
 * Customize the player view
 * To display in Listview or Gridview
 */

public class PlayerAdapter extends ArrayAdapter<Player> {

    private final String TAG = this.getClass().getSimpleName();
    private RestPlayerService restPlayerService;

    public PlayerAdapter(@NonNull Context context, int resource, @NonNull List<Player> players) {
        super(context, resource, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        restPlayerService = new RestPlayerService();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_player_entry, parent, false);
        }

        final Player player = getItem(position);

        if (player != null) {
            TextView tvPlayerId = (TextView) v.findViewById(R.id.player_Id);
            TextView tvPlayerName = (TextView) v.findViewById(R.id.player_name);
            ImageView ivPlayerLogo = (ImageView) v.findViewById(R.id.player_logo);
            tvPlayerId.setText( player.Id);
            // set player id in center
            tvPlayerId.setGravity(Gravity.CENTER);
            tvPlayerName.setText(player.Name);
            // set player name in center
            tvPlayerName.setGravity(Gravity.CENTER);
            if(player.Avatar!=null){
                String playerLogoUrl = player.Avatar.replaceAll("localhost","10.0.2.2");
                Log.d(TAG,playerLogoUrl);

                // Get image data from playerLogoUrl and pass to ImageView ivPlayerLogo
                Picasso.with(this.getContext())
                        .load(playerLogoUrl)
                        .resize(50,50)
                        .centerCrop()
                        .into(ivPlayerLogo);

            }else {
                // Set default player logo
                ivPlayerLogo.setImageResource(R.drawable.player); 
            }




        }

        final Button btnDel = (Button) v.findViewById(R.id.btnDelPlayer);
        // Set onclick event
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(player);

            }
        }); 

        return v;
    }

    // TO DO when delete
    private void delete(final Player player){
        restPlayerService.getService().deletePlayer(player.Id, new Callback<List<Player>>() {
            @Override
            public void success(List<Player> players, Response response) {
                Log.d(TAG,"Delete player done successfully!");
                remove(player);
                // notify changes
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG,"Delete player failed!");
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
