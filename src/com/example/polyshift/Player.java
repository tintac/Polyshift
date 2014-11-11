package com.example.polyshift;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

public class Player {
	
	private Bitmap bitmap;
	private int TextureID;
	private Texture texture;
	private Vector position;
	private Mesh mesh;
	private GL10 gl;
	private GameActivity activity;
	public static float size = 100;
	public boolean isMoving = false;
	
	public Player(GameActivity activity, GL10 gl, float x, float y, float z){
		this.position = new Vector(x,y,z);
		this.gl = gl;
		this.activity = activity;
		
		try
		{
		   bitmap = null;
		   bitmap = BitmapFactory.decodeStream( activity.getAssets().open( "droid.png" ) );
		}
		catch( Exception ex )
		{
			Log.d("Sample", "oh noes!");
            System.exit(-1);
		}	
		
		mesh = new Mesh( gl, 6, false, true, false );				
		mesh.texCoord(0f, 1f);
		mesh.vertex( position.x, position.y, 0 );
		mesh.texCoord(1f, 1f);
        mesh.vertex( position.x + size, position.y, 0 );
        mesh.texCoord(1f, 0f);
        mesh.vertex( position.x + size, position.y + size, 0 );
        mesh.texCoord(0f, 0f);
        mesh.vertex( position.x, position.y + size, 0 );
        
		texture = new Texture(gl, bitmap, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	}
	
	public void drawPlayer(){
		
		int velocity = 0;
		if((this.position.x < activity.getViewportWidth() - size - 25) && isMoving){
			velocity = 25;
		}
		else{
			isMoving = false;
		}
		gl.glPushMatrix();
		gl.glTranslatef(position.x + velocity, 0, 0 );
		texture.bind();
		mesh.render(PrimitiveType.TriangleFan);
		gl.glPopMatrix();
		this.position.x += velocity;
		Log.d( "Obj Sample", "X: " + position.x );	
	}
	

}
