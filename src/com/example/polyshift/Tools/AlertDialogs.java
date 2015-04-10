package com.example.polyshift.Tools;

import java.util.Calendar;

import com.example.polyshift.Menu.WelcomeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;

public class AlertDialogs {
	
	public Activity activity;
	public String[] article;
	public String store;
	public String date;
	public Calendar new_date;
	static AlertDialog alert;
	
	public AlertDialogs(Activity activity){
		this.activity = activity;
		this.article = new String[3];
	}
	public static void showAlert(final Activity ParentActivity, final String title, final String message){
        ParentActivity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentActivity);
                builder.setTitle(title);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
	public static void showLoginAgainAlert(final Activity ParentActivity){
        ParentActivity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentActivity);
                builder.setTitle("Sitzung abgelaufen");
                builder.setMessage("Bitte erneut anmelden.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            	Intent intent = new Intent(ParentActivity, WelcomeActivity.class);
                                ParentActivity.startActivity(intent);
                            }
                        });
                alert = builder.create();
                alert.show();
            }
        });
    }
}
