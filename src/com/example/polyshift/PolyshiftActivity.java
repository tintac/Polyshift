package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class PolyshiftActivity extends GameActivity implements GameListener {

	Player player;
	Player player2;
	Polynomio poly;
	Renderer renderer;
	Simulation simulation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
   	 setRequestedOrientation(0);
     requestWindowFeature(Window.FEATURE_NO_TITLE);
     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		 
		
		super.onCreate(savedInstanceState);
		
		setGameListener(this);
	}

	@Override
	public void setup(GameActivity activity, GL10 gl) {
		
		simulation = new Simulation(activity);
		renderer = new Renderer(activity, gl, simulation.objects);
	}
	  
	long start = System.nanoTime();
    int frames = 0;
	
	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) {
		
		gl.glViewport(0, 0, activity.getViewportWidth(), activity.getViewportHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable( GL10.GL_TEXTURE_2D );
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		GLU.gluOrtho2D( gl, 0, activity.getViewportWidth(), 0, activity.getViewportHeight());
		
		gl.glMatrixMode( GL10.GL_MODELVIEW );
        gl.glLoadIdentity();
		
        simulation.update(activity);
        
        renderer.renderObjects(activity, gl, simulation.objects);
        
        frames++;
        if( System.nanoTime() - start > 1000000000 )
        {
                frames = 0;
                start = System.nanoTime();
        }

	}
}
