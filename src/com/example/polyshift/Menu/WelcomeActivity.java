package com.example.polyshift.Menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polyshift.R;
import com.example.polyshift.Tools.PasswordHash;
import com.example.polyshift.Tools.PHPConnector;
import com.example.polyshift.PolyshiftActivity;
import com.example.polyshift.Tools.AlertDialogs;

public class WelcomeActivity extends Activity {
	
	Button loginButton;
    EditText editUsername,editPassword;
    TextView debugText;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    protected static ProgressDialog dialog = null;
    private static Context context;

//    public WelcomeActivity(){
//    	Log.d("WelcomeActivity","constructor");
//    }
    
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context=getApplicationContext();
		
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		setContentView(R.layout.activity_welcome);

		loginButton = (Button)findViewById(R.id.LoginButton);
        editUsername = (EditText)findViewById(R.id.EditUsername);
        editPassword= (EditText)findViewById(R.id.EditPassword);

        loginButton.setOnClickListener(
    		new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog = ProgressDialog.show(WelcomeActivity.this, "","Login läuft", true);
	                new Thread(
	                		new Runnable(){
	                			public void run(){
	                				userLogin(editUsername.getText().toString(),editPassword.getText().toString(),WelcomeActivity.this);
	                			}
	                		}
	                ).start();
	            }
    		}
    	);
	}
    static void userLogin(final String username, String password, final Activity activity){
	   		 
   		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
         nameValuePairs.add(new BasicNameValuePair("username",username.toString().trim()));
         nameValuePairs.add(new BasicNameValuePair("password",PasswordHash.toHash(password.toString().trim())));
   		 
         final String response = PHPConnector.doRequest(nameValuePairs, "login_user.php");
         if(response.equalsIgnoreCase(username + " has logged in successfully.")){
        	 activity.runOnUiThread(new Runnable() {
                 public void run() {
                     Toast.makeText(activity,username + " wurde erfolgreich angemeldet.", Toast.LENGTH_SHORT).show();
                 }
             });
        	 
        	 //HandleSharedPreferences.setUserCredentials(context, username, password);	//	username und pw werden gespeichert, damit beim nächsten Mal kein Login notwendig ist
        	 
             Intent intent = new Intent(activity, MainMenuActivity.class);
             activity.startActivity(intent);
         }
         else if(response.equalsIgnoreCase(username + " already logged in.")){
        	 activity.runOnUiThread(new Runnable() {
                 public void run() {
                     Toast.makeText(activity,response, Toast.LENGTH_SHORT).show();
                 }
             });
             Intent intent = new Intent(activity, MainMenuActivity.class);
             activity.startActivity(intent);
         }else if(response.equalsIgnoreCase("No Such User Found")){
        	 dialog.dismiss();
             AlertDialogs.showAlert(activity,"Login Error","User not found or password incorrect.");
         }else{
            dialog.dismiss();
            AlertDialogs.showAlert(activity,"Login Error","Connection Error.");
         }
    }

    public void userSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}