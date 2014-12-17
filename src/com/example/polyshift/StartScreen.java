package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.util.Log;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

public class StartScreen implements GameScreen {

	boolean isDone = false;
	Mesh backgroundMesh;
	Texture backgroundTexture;
	
	
	public StartScreen( GL10 gl, GameActivity activity )
	{			
		backgroundMesh = new Mesh( gl, 4, true, true, false );
		backgroundMesh.color(238f/255f/1.1f, 233f/255f/1.1f, 192f/255f/1.1f, 1.0f );
		backgroundMesh.texCoord(0, 0);
		backgroundMesh.vertex(-1, 1, 0 );
		backgroundMesh.color(238f/255f/1.1f, 233f/255f/1.1f, 192f/255f/1.1f, 1.0f );
		backgroundMesh.texCoord(1, 0);
		backgroundMesh.vertex(1, 1, 0 );
		backgroundMesh.color(238f/255f/1.1f, 233f/255f/1.1f, 192f/255f/1.1f, 1.0f );
		backgroundMesh.texCoord(1, 1);
		backgroundMesh.vertex(1, -1, 0 );
		backgroundMesh.color(238f/255f/1.1f, 233f/255f/1.1f, 192f/255f/1.1f, 1.0f );
		backgroundMesh.texCoord(0, 1);
		backgroundMesh.vertex(-1, -1, 0 );
		
		try
		{
			Bitmap bitmap = BitmapFactory.decodeStream( activity.getAssets().open( "SplashScreen.png" ) );
			backgroundTexture = new Texture( gl, bitmap, TextureFilter.MipMap, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			
			
		}
		catch( Exception ex )
		{
			Log.d( "Startscreen", "Startscreen texture could not be loaded" );
			throw new RuntimeException( ex );
		}
	}	

	@Override
	public boolean isDone() 
	{	
		return isDone;
	}

	@Override
	public void update(GameActivity activity) 
	{	
		if( activity.isTouched() )
			isDone = true;
	}
	
	@Override
	public void render(GL10 gl, GameActivity activity) 
	{	
		gl.glViewport( 0, 0, activity.getViewportWidth(), activity.getViewportHeight() );
		gl.glClearColor( 238f/255f/1.1f, 233f/255f/1.1f, 192f/255f/1.1f, 0.0f );
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		float aspectRatio = (float)activity.getViewportWidth() / activity.getViewportHeight();
		GLU.gluPerspective( gl, 67, aspectRatio, 1, 100 );
		GLU.gluLookAt(gl, 5.5f, 3.38f, 4.8f, 5.5f, 3.38f, -5f, 0f, 1f, 0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glLoadIdentity();
		backgroundTexture.bind();
		backgroundMesh.render(PrimitiveType.TriangleFan );
		gl.glDisable( GL10.GL_TEXTURE_2D );
	}



	
}
