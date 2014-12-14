package com.example.polyshift;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.util.Log;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;
import com.example.polyshift.Tools.MeshLoader;

public class Renderer3D extends Renderer {
	
	float object_depth;
	float width = 17f;
	float height = 10f;
	
	
	public Renderer3D(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		block_width = width / objects.length;
		block_height = height / objects[0].length;
		
		object_width = width / objects.length;
		object_height = height / objects[0].length;
		
		object_depth = -0.5f;
		
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					
					
					try {
						Mesh mesh;
						mesh = MeshLoader.loadObj(gl, activity.getAssets().open( "kugel.obj" ) );
						
						objects[i][j].setMesh(mesh);
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					/*
					Bitmap bitmapPlayerOne = null;
					Bitmap bitmapPlayerTwo = null;
					Bitmap bitmapPlayerOneLock = null;
					Bitmap bitmapPlayerTwoLock = null;

					
					try
					{
					    bitmapPlayerOne = BitmapFactory.decodeStream( activity.getAssets().open( "player1.png" ) );
					    bitmapPlayerOneLock = BitmapFactory.decodeStream( activity.getAssets().open( "player1_lock.png" ) );
					    bitmapPlayerTwo = BitmapFactory.decodeStream( activity.getAssets().open( "player2.png" ) );
					    bitmapPlayerTwoLock = BitmapFactory.decodeStream( activity.getAssets().open( "player2_lock.png" ) );
					}
					catch( Exception ex )
					{
						Log.d("Sample", "Failed loading player texture.");
			            System.exit(-1);
					}	
					
					Mesh mesh;
					mesh = new Mesh( gl, 4, false, true, false );				
					mesh.texCoord(0f, 1f);
					mesh.vertex( i/32*block_width, j/16*block_height, object_depth );
					mesh.texCoord(1f, 1f);
			        mesh.vertex( i/32*block_width + object_width, j/16*block_height, object_depth );
			        mesh.texCoord(1f, 0f);
			        mesh.vertex( i/32*block_width + object_width, j/16*block_height + object_height, object_depth );
			        mesh.texCoord(0f, 0f);
			        mesh.vertex( i/32*block_width, j/16*block_height + object_height, object_depth );
			        
			        if(objects[i][j].isPlayerOne){
			        	texturePlayerOne = new Texture(gl, bitmapPlayerOne, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        	texturePlayerOneLock = new Texture(gl, bitmapPlayerOneLock, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        }else{
			        	texturePlayerTwo = new Texture(gl, bitmapPlayerTwo, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        	texturePlayerTwoLock = new Texture(gl, bitmapPlayerTwoLock, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        }
			        
			        objects[i][j].setMesh(mesh);
			        
			        */
				}
				if(objects[i][j] instanceof Polynomio){
					Bitmap bitmapLocker = null;
					
					try
					{
					    bitmapLocker = BitmapFactory.decodeStream( activity.getAssets().open( "locker.png" ) );
					}
					catch( Exception ex )
					{
						Log.d("Sample", "Failed loading locker texture.");
			            System.exit(-1);
					}
					
					Mesh mesh;
					mesh = new Mesh( gl, 16, false, false, false );
			        
					mesh.vertex(i/32*block_width + object_width, j/16*block_height, 0f);
					
					mesh.vertex(i/32*block_width, j/16*block_height, 0f);
					
					mesh.vertex(i/32*block_width + object_width, j/16*block_height, object_depth);
					
					mesh.vertex(i/32*block_width, j/16*block_height, object_depth);
					
					mesh.vertex(i/32*block_width, j/16*block_height, object_depth);
					
					mesh.vertex(i/32*block_width, j/16*block_height, 0);
					
					mesh.vertex(i/32*block_width, +j/16*block_height + object_height, 0);
					
					mesh.vertex(i/32*block_width + object_width, j/16*block_height, 0f);
					
					mesh.vertex(i/32*block_width + object_width, j/16*block_height + object_height, 0f);
					
					mesh.vertex(i/32*block_width + object_width, j/16*block_height, object_depth);
					
					mesh.vertex(i/32*block_width + object_width, j/16*block_height + object_height, object_depth);
					
					mesh.vertex(i/32*block_width, j/16*block_height + object_height, object_depth);
					
					mesh.vertex(i/32*block_width + object_width, j/16*block_height + object_height, 0f);
					
					mesh.vertex(i/32*block_width, j/16*block_height + object_height, 0f);
					
					mesh.vertex(i/32*block_width, j/16*block_height + object_height, object_depth);
					
					mesh.vertex(i/32*block_width, j/16*block_height, object_depth);
			        
			        textureLocker = new Texture(gl, bitmapLocker, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        
			        objects[i][j].setMesh(mesh);
			        
				}
			}
		}
		
	}
	
	public void setPerspective(GameActivity activity, GL10 gl){
		    
        gl.glViewport( 0, 0, activity.getViewportWidth(), activity.getViewportHeight() );
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT  | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		float aspectRatio = (float)activity.getViewportWidth() / activity.getViewportHeight();
		GLU.gluPerspective( gl, 67, aspectRatio, 1, 100 );
		gl.glTranslatef(-8.5f, -5.0f, -7.3f);	
		
	    gl.glEnable(GL10.GL_DEPTH_TEST);
	}
	
	public void renderObjects(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		if(coordinates_list != null){
			for(Mesh mesh : coordinates_list){
				mesh.render( PrimitiveType.Lines );
			}
		}
	
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					if(objects[i][j].isPlayerOne){
						if(objects[i][j].isLocked){
							gl.glColor4f(1f,0f,0f,1f);
						}
						else{
							gl.glColor4f(1f,0f,0f,1f);
						}
					}else{
						if(objects[i][j].isLocked){
							gl.glColor4f(1f,1f,0f,1f);
						}
						else{
							gl.glColor4f(1f,1f,0f,1f);
						}
					}
					if(objects[i][j].isMovingLeft){
						if(objects[i][j].pixel_position.x == -1){
							objects[i][j].pixel_position.x = objects[i][j].block_position.x*block_width;
						}
						if(objects[i][j].pixel_position.x > i*block_width){
							renderPlayer(gl, objects[i][j],objects[i][j].pixel_position.x, j*block_height, 0 );
							objects[i][j].pixel_position.x -= block_width * objects[i][j].movingVelocity;
						}
						else{
							objects[i][j].isMovingLeft = false;
							objects[i][j].lastState = Simulation.LEFT;
							objects[i][j].pixel_position.x = -1;
						}
					}
					else if(objects[i][j].isMovingRight){
						if(objects[i][j].pixel_position.x == -1){
							objects[i][j].pixel_position.x = objects[i][j].block_position.x*block_width;
						}
						if(objects[i][j].pixel_position.x < i*block_width){
							renderPlayer(gl, objects[i][j],objects[i][j].pixel_position.x, j*block_height, 0 );
							objects[i][j].pixel_position.x += block_width * objects[i][j].movingVelocity;
						}
						else{
							objects[i][j].isMovingRight = false;
							objects[i][j].lastState = Simulation.RIGHT;
							objects[i][j].pixel_position.x = -1;
						}
					}
					else if(objects[i][j].isMovingUp){
						if(objects[i][j].pixel_position.y == -1){
							objects[i][j].pixel_position.y = objects[i][j].block_position.y*block_height;
						}
						if(objects[i][j].pixel_position.y < j*block_height){
							renderPlayer(gl, objects[i][j],i*block_width,objects[i][j].pixel_position.y, 0 );
							objects[i][j].pixel_position.y += block_height * objects[i][j].movingVelocity;
						}
						else{
							objects[i][j].isMovingUp = false;
							objects[i][j].lastState = Simulation.UP;
							objects[i][j].pixel_position.y = -1;
						}
					}
					else if(objects[i][j].isMovingDown){
						if(objects[i][j].pixel_position.y == -1){
							objects[i][j].pixel_position.y = objects[i][j].block_position.y*block_height;
						}
						if(objects[i][j].pixel_position.y > j*block_height){
							renderPlayer(gl, objects[i][j],i*block_width,objects[i][j].pixel_position.y, 0 );
							objects[i][j].pixel_position.y -= block_height * objects[i][j].movingVelocity;
						}
						else{
							objects[i][j].isMovingDown = false;
							objects[i][j].lastState = Simulation.DOWN;
							objects[i][j].pixel_position.y = -1;
						}
					}
					else{
						renderPlayer(gl, objects[i][j],i*block_width, j*block_height, 0 );
					}
				}
				if(objects[i][j] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[i][j];
					/*if(polynomio.isLocked){
						gl.glEnable( GL10.GL_TEXTURE_2D );
						gl.glEnable(GL10.GL_BLEND);
						gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
						textureLocker.bind();
						gl.glColor4f(objects[i][j].colors[0],objects[i][j].colors[1],objects[i][j].colors[2],objects[i][j].colors[3]);
						gl.glPushMatrix();
						gl.glTranslatef(i*block_width, j*block_height, 0 );
						objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
						gl.glPopMatrix();
						gl.glColor4f(1, 1, 1, 1);
						gl.glDisable(GL10.GL_BLEND);
						gl.glDisable( GL10.GL_TEXTURE_2D );
					}
					else{*/
						gl.glColor4f(objects[i][j].colors[0],objects[i][j].colors[1],objects[i][j].colors[2],objects[i][j].colors[3]);
						gl.glPushMatrix();
						gl.glFrontFace(GL10.GL_CW);
						gl.glTranslatef(i*block_width, j*block_height, 0 );
						objects[i][j].getMesh().render(PrimitiveType.TriangleStrip);
						gl.glPopMatrix();
						gl.glColor4f(1, 1, 1, 1);
					//}
				}
			}
		}
	}
	
	public void renderPlayer(GL10 gl, GameObject player, float x, float y, float z){
		gl.glEnable(GL10.GL_NORMALIZE);
		gl.glPushMatrix();
		gl.glTranslatef(x,y,z );
		gl.glScalef(0.5f,0.5f,0.5f);
		player.getMesh().render(PrimitiveType.TriangleFan);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_NORMALIZE);
	}
	
	
	public void enableCoordinates(GL10 gl, GameObject[][] objects){
		coordinates_list = new ArrayList<Mesh>();
		
		for(int x = 0; x < objects.length + 1; x++){
			Mesh mesh = new Mesh(gl, 2, true, false, false);
			mesh.color( 0, 1, 0, 1 );
			mesh.vertex( block_width * x, 0, object_depth );
			mesh.color( 0, 1, 0, 1 );
			mesh.vertex( block_width * x, block_height * objects[0].length, object_depth );
			mesh.render( PrimitiveType.Lines );
			coordinates_list.add(mesh);
		}
		for(int y = 0; y < objects[0].length + 1; y++){
			Mesh mesh = new Mesh(gl, 2, true, false, false);
			mesh.color( 0, 1, 0, 1 );
			mesh.vertex( 0, block_height * y, object_depth);
			mesh.color( 0, 1, 0, 1 );
			mesh.vertex( block_width * objects.length, block_height * y, object_depth );
			mesh.render( PrimitiveType.Lines );
			coordinates_list.add(mesh);
		}
	}
}
