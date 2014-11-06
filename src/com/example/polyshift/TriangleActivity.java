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

public class TriangleActivity extends GameActivity implements GameListener {

	private FloatBuffer vertices;
	private FloatBuffer colors;
	private FloatBuffer texCoords;
	private Bitmap bitmap;
	private int TextureID;
	private Mesh mesh;
	private Texture texture;
	private boolean start = true;
	private boolean touched = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setGameListener(this);
	}

	@Override
	public void setup(GameActivity activity, GL10 gl) {
		try
		{
		   bitmap = null;
		   bitmap = BitmapFactory.decodeStream( getAssets().open( "droid.png" ) );
		}
		catch( Exception ex )
		{
			Log.d("Sample", "oh noes!");
            System.exit(-1);
		}	
		
		mesh = new Mesh( gl, 6, false, true, false );				
		mesh.texCoord(0f, 1f);
		mesh.vertex( 0, 0, 0 );
		mesh.texCoord(1f, 1f);
        mesh.vertex( 400, 0, 0 );
        mesh.texCoord(1f, 0f);
        mesh.vertex( 400, 400, 0 );
        mesh.texCoord(0f, 0f);
        mesh.vertex( 0, 400, 0 );
        
		texture = new Texture(gl, bitmap, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	
	}
	      
	

	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) {
		gl.glViewport(0, 0, activity.getViewportWidth(), activity.getViewportHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable( GL10.GL_TEXTURE_2D );
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		GLU.gluOrtho2D( gl, 0, activity.getViewportWidth(), 0, activity.getViewportHeight());
		
		
		
		
		if(start){
			texture.bind();
			mesh.render(PrimitiveType.TriangleFan);
		}	
			
		
		
		if(isTouched()){
			touched = true;
			start = false;
		}
		
		if(touched){
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glPushMatrix();
			
			gl.glTranslatef( 600, 0, 0 );
			mesh.render(PrimitiveType.TriangleFan);
			gl.glPopMatrix();
			
			
			
		}

	}
}
