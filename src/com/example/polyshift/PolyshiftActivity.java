package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class PolyshiftActivity extends GameActivity implements GameListener {

	Player player;
	Player player2;
	Polynomio poly;
	Renderer renderer;
	StartScreen startScreen;
	EndScreen endScreen;
	Simulation simulation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		super.onCreate(savedInstanceState);
		
		setGameListener(this);
	}

	@Override
	public void setup(GameActivity activity, GL10 gl) {
		
		if(!(simulation instanceof Simulation)){
			startScreen = new StartScreen(gl, activity);
			endScreen = new EndScreen(gl, activity);
			simulation = new Simulation(activity);
			renderer = new Renderer(activity, gl, simulation.objects);
		}	
	}
	  
	long start = System.nanoTime();
    int frames = 0;
	
	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) {
		if(!startScreen.isDone){
			startScreen.update(activity);
			startScreen.render(gl, activity);
		}
		else{
			renderer.setPerspective(activity, gl);
			renderer.renderObjects(activity, gl, simulation.objects);
			simulation.update(activity);
			
			if(simulation.hasWinner){
				endScreen.setWinner(simulation.winner);
				endScreen.render(gl, activity);
				endScreen.update(activity);

				if(activity.isTouched()){
					activity.finish();
					startActivity(activity.getIntent());
				}
			}
			
		}
			
        frames++;
        if( System.nanoTime() - start > 1000000000 )
        {
                frames = 0;
                start = System.nanoTime();
        }

	}
}
