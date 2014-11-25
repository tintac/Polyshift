package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Bundle;

import com.example.polyshift.GameActivity;
import com.example.polyshift.GameListener;
import com.example.polyshift.Mesh;
import com.example.polyshift.Mesh.PrimitiveType;

public class PerspectiveSample extends GameActivity implements GameListener 
{
    Mesh mesh;
    Mesh mesh2;
    Mesh mesh3;
    Mesh mesh4;
	
	public void onCreate( Bundle savedInstance )
	{
		super.onCreate( savedInstance );
		setGameListener( this );
	}

	@Override
	public void setup(GameActivity activity, GL10 gl) 
	{	
		// we create a mesh with 3 vertices having different colors
		
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT,
		          new float[] { 0.2f, 0.2f, 0.2f, 1.0f }, 0);
		gl.glEnable(GL10.GL_LIGHTING);
      gl.glEnable(GL10.GL_LIGHT0);
      
      gl.glEnable(GL10.GL_DEPTH_TEST);
      gl.glEnable(GL10.GL_BLEND);
      gl.glBlendFunc(GL10.GL_SRC_ALPHA,
          GL10.GL_ONE_MINUS_SRC_ALPHA);
      gl.glEnable(GL10.GL_MULTISAMPLE);
		
		mesh = new Mesh( gl, 2, true, false, false );		
		mesh.color( 0, 1, 0, 1 );
		mesh.vertex( 0f, 0f, 0f );
		mesh.color( 0, 1, 0, 1 );
		mesh.vertex( 9.6f, 0f, 0f );
		
		mesh2 = new Mesh( gl, 2, true, false, false );		
		mesh2.color( 1, 0, 0, 1 );
		mesh2.vertex( 0f, 0f, 0f );
		mesh2.color( 1, 0, 0, 1 );
		mesh2.vertex( 0f, 5.2f, 0f );
		
		mesh3 = new Mesh( gl, 2, true, false, false );		
		mesh3.color( 0, 0, 1, 1 );
		mesh3.vertex( 0f, 0f, 0f );
		mesh3.color( 0, 0, 1, 1 );
		mesh3.vertex( 0f, 0f, -20f );
		
		mesh4 = new Mesh( gl, 14, true, false, false );		
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, 0f, 0f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, 0f, 0f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, 0f, -0.1f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, 0f, -0.1f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, 0f, -0.1f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, 0f, 0);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, +0.5f, 0);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, 0f, 0f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, +0.5f, 0f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, 0f, -0.1f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, +0.5f, -0.1f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, +0.5f, -0.1f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(+0.5f, +0.5f, 0f);
		mesh4.color( 0, 1, 1, 1);
		mesh4.vertex(0f, +0.5f, 0f);
		
		
		
	}
	
	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) 
	{	
		gl.glViewport( 0, 0, activity.getViewportWidth(), activity.getViewportHeight() );
		  gl.glClear(GL10.GL_COLOR_BUFFER_BIT
		          | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		float aspectRatio = (float)activity.getViewportWidth() / activity.getViewportHeight();
		GLU.gluPerspective( gl, 67, aspectRatio, 1, 100 );
		
		gl.glTranslatef(-4.8f, -2.6f, -4.0f);
		
		
		
		mesh.render( PrimitiveType.Lines );
		mesh2.render( PrimitiveType.Lines );
		mesh3.render( PrimitiveType.Lines );
		mesh4.render( PrimitiveType.TriangleStrip );
	}
}
