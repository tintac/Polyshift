package com.example.polyshift.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.polyshift.Menu.GamesAttendingActivity;
import com.example.polyshift.PolyshiftActivity;
import com.example.polyshift.R;
import com.example.polyshift.Tools.PHPConnector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGamesAdapter extends SimpleAdapter {
    private LayoutInflater inflater;
    private Context context;
    public ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
    public String response;

	public MyGamesAdapter(Context context,
                          List<? extends Map<String, ?>> data, int resource, String[] from,
                          int[] to) {
		super(context, data, resource, from, to);
		this.inflater = LayoutInflater.from(context);
		this.data = (ArrayList<HashMap<String, String>>) data;
        this.context = context;
        this.response = "";
		// TODO Auto-generated constructor stub
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_my_games_item, null);
        }
        if(!data.isEmpty()) {

            final TextView opponent_view = (TextView) convertView.findViewById(R.id.opponent);
            opponent_view.setText(data.get(position).get("opponent_name"));

            final TextView status_view = (TextView) convertView.findViewById(R.id.status);
            if (data.get(position).get("my_game").equals("yes") && data.get(position).get("game_accepted").equals("0")) {
                status_view.setText("Warten auf Annahme");
            } else if (data.get(position).get("opponents_turn").equals("0")) {
                status_view.setText("Du bist dran!");
            } else {
                status_view.setText(data.get(position).get("opponent_name") + " macht einen Zug.");
            }
            status_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(
                            new Runnable(){
                                public void run(){
                                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                    nameValuePairs.add(new BasicNameValuePair("game", data.get(position).get("game_id")));
                                    PHPConnector.doRequest(nameValuePairs, "update_game.php");
                                }
                            }

                    ).start();
                    final Intent intent = new Intent(context, PolyshiftActivity.class);
                    context.startActivity(intent);
                }
            });
        }
	    return convertView; 
    }
}
