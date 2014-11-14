package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.content.pm.ActivityInfo;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class PolyshiftActivity extends GameActivity implements GameListener {

	Player player;
	Player player2;
	Polynomio poly;
	Renderer renderer;
	StartScreen startScreen;
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
			simulation = new Simulation(activity);
			renderer = new Renderer(activity, gl, simulation.objects);
		}
			
	}
	  
	long start = System.nanoTime();
    int frames = 0;
	
	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) {
		if(startScreen.isDone){
			simulation.update(activity);
			renderer.setPerspective(activity, gl);
			renderer.renderObjects(activity, gl, simulation.objects);
		}else{
			startScreen.update(activity);
			startScreen.render(gl, activity);
			
		}
			
        frames++;
        if( System.nanoTime() - start > 1000000000 )
        {
                frames = 0;
                start = System.nanoTime();
        }

	}
}
