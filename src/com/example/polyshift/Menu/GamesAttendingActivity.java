package com.example.polyshift.Menu;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.polyshift.Adapter.AcceptGameAdapter;
import com.example.polyshift.Adapter.AcceptOpponentAdapter;
import com.example.polyshift.R;
import com.example.polyshift.Tools.AlertDialogs;
import com.example.polyshift.Tools.PHPConnector;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class GamesAttendingActivity extends ListActivity {

    public static AcceptGameAdapter mAdapter;
    public static ProgressDialog dialog = null;
    public String response = "";


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_opponents_attending);
       setTitle(getString(R.string.new_game));

       if (MyGamesActivity.games_attending_list != null && MyGamesActivity.games_attending_list.size() != 0) {

	        mAdapter = new AcceptGameAdapter(this,
                    MyGamesActivity.games_attending_list,
	        		 R.layout.activity_choose_opponent_item,
	                 new String[] {"opponent_name"},
	                 new int[] {R.id.title});

	       ListView listView = this.getListView();
	       setListAdapter(mAdapter);
	       listView.setFocusableInTouchMode(false);
	       listView.setFocusable(false);
	       listView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	SaveValue.setSelectedFriendName(MyGamesActivity.games_attending_list.get(position).get("opponent_name"));

	            	Intent intent = new Intent(GamesAttendingActivity.this, MyGamesActivity.class);
		            startActivity(intent);
	            }
	        });

       }
	}

	// Action Bar Button
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_opponents_attending, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
        switch (item.getItemId()) {
            case R.id.action_accept:
                //dialog = ProgressDialog.show(OpponentsAttendingActivity.this, "", getString(R.string.dialog_opponent_handling), true);

                Thread add_games_thread = new AddGamesThread();
                add_games_thread.start();
                try {
                    long waitMillis = 10000;
                    while (add_games_thread.isAlive()) {
                        add_games_thread.join(waitMillis);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (response.equals("opponent accepted")) {
                    Log.d("res:", response);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Herausforderung wurde angenommen.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final Intent intent = new Intent(GamesAttendingActivity.this, ChooseOpponentActivity.class);
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                    });
                    builder.show();

                } else {
                    AlertDialogs.showAlert(GamesAttendingActivity.this, "Fehler", "Das Spiel konnte nicht gestartet werden.");
                }
                break;

	        case R.id.action_decline:
                dialog = ProgressDialog.show(GamesAttendingActivity.this, "", getString(R.string.dialog_opponent_handling), true);
                new Thread(
                    new Runnable(){
                        public void run(){
                            for(String user: mAdapter.getCheckedGameIDs()) {
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("opponent", user));
                                PHPConnector.doRequest(nameValuePairs, "decline_game.php");
                            }
                        }
                    }
                ).start();
                 intent = new Intent(GamesAttendingActivity.this, MainMenuActivity.class);
                startActivity(intent);
                break;

                default:
                break;

        }
	          return true;

    }
    public class AddGamesThread extends Thread{
        public void run() {
            for (String user : mAdapter.getCheckedGameIDs()) {
                Log.d("user",user);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("opponent", user));
                response = PHPConnector.doRequest(nameValuePairs, "accept_game.php");
            }
        }
    }
}
