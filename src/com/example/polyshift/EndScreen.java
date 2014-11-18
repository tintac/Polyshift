package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.TextElementListener;
import android.util.Log;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

public class EndScreen implements GameScreen {

	private Player winner;
	public boolean isDone = false;
	Mesh pictureMesh;
	Texture textureWinnerPlayerTwo;
	Texture textureWinnerPlayerOne;
	
	public EndScreen( GL10 gl, GameActivity activity )
	{			
		pictureMesh = new Mesh( gl, 4, false, true, false );
		pictureMesh.texCoord(0, 0);
		pictureMesh.vertex(-1, 1, 0 );
		pictureMesh.texCoord(1, 0);
		pictureMesh.vertex(1, 1, 0 );
		pictureMesh.texCoord(1, 1);
		pictureMesh.vertex(1, -1, 0 );
		pictureMesh.texCoord(0, 1);
		pictureMesh.vertex(-1, -1, 0 );
		
		try
		{
			Bitmap bitmap = BitmapFactory.decodeStream( activity.getAssets().open( "winnerPlayerOne.png" ) );
			textureWinnerPlayerOne = new Texture( gl, bitmap, TextureFilter.MipMap, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			bitmap = BitmapFactory.decodeStream( activity.getAssets().open( "winnerPlayerTwo.png" ) );
			textureWinnerPlayerTwo = new Texture( gl, bitmap, TextureFilter.MipMap, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			
			
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
		if(winner.isPlayerOne){
			textureWinnerPlayerOne.bind();
		}else{
			textureWinnerPlayerTwo.bind();
		}
		
		pictureMesh.render(PrimitiveType.TriangleFan );
		gl.glDisable( GL10.GL_TEXTURE_2D );
	}

	public void setWinner(Player winner) {
		// TODO Auto-generated method stub
		this.winner = winner;
	}




}
