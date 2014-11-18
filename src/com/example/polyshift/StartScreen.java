package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		backgroundMesh = new Mesh( gl, 4, false, true, false );
		backgroundMesh.texCoord(0, 0);
		backgroundMesh.vertex(-1, 1, 0 );
		backgroundMesh.texCoord(1, 0);
		backgroundMesh.vertex(1, 1, 0 );
		backgroundMesh.texCoord(1, 1);
		backgroundMesh.vertex(1, -1, 0 );
		backgroundMesh.texCoord(0, 1);
		backgroundMesh.vertex(-1, -1, 0 );
		
		try
		{
			Bitmap bitmap = BitmapFactory.decodeStream( activity.getAssets().open( "SplashScreen.png" ) );
			backgroundTexture = new Texture( gl, bitmap, TextureFilter.MipMap, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			
			
		}
		catch( Exception ex )
		{
			Log.d( "Space Invaders", "couldn't load textures" );
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
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
		backgroundTexture.bind();
		backgroundMesh.render(PrimitiveType.TriangleFan );
		gl.glDisable( GL10.GL_TEXTURE_2D );
	}



	
}
