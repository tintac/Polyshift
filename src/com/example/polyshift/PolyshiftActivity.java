package com.example.polyshift;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PolyshiftActivity extends GameActivity implements GameListener {

	Player player;
	Player player2;
	private boolean touched = false;
	private boolean started = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setGameListener(this);
	}

	@Override
	public void setup(GameActivity activity, GL10 gl) {
		
		player = new Player(activity, gl, 0, 400, 0);
		player2 = new Player(activity, gl, activity.getViewportWidth()-Player.size, 400, 0);
		
		
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
		
	
       
		
			
		player.drawPlayer();
		player2.drawPlayer();
	
		
			
		
			
		
		
		if(isTouched()){
			touched = true;
		}
		
		if(touched){
			
			player.isMoving = true;
			
			touched = false;
			
			
			
		}
		

        frames++;
        if( System.nanoTime() - start > 1000000000 )
        {
                Log.d( "Obj Sample", "fps: " + frames );
                Log.d( "Obj Sample", "Delta: " + activity.getDeltaTime() );
                frames = 0;
                start = System.nanoTime();
        }

	}
}
