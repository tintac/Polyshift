package com.example.polyshift;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		Intent intent = new Intent( this, PolyshiftActivity.class );
		
		startActivity( intent );
		
	}

		
}
