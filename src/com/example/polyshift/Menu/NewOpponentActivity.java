package com.example.polyshift.Menu;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.polyshift.R;
import com.example.polyshift.Tools.PHPConnector;

public class NewOpponentActivity extends Activity {
	
	private String username;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_opponent);
        setTitle(R.string.new_opponent);
        
        EditText usernameTextfield = (EditText)findViewById(R.id.contacts_new_enterusername);
        usernameTextfield.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				username = arg0.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
        
	});}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_opponent, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_addopponent:
	        		Thread thread = new Thread(new Runnable(){
						@Override
						public void run() {
				        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("opponent",username));
							
							PHPConnector.doRequest(nameValuePairs, "add_opponent.php");
						}
	        		});
	        		thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        	Intent intent = new Intent(this, MainMenuActivity.class);
	            startActivity(intent);
	            
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
