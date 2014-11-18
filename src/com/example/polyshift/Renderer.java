package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLU;
import android.util.Log;
import android.view.Display;

public class Renderer {
	
	float block_width;
	float block_height;
	float object_width;
	float object_height;
	int count = 0;
	
	public Renderer(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		block_width = activity.getViewportWidth() / objects.length;
		block_height = activity.getViewportHeight() / objects[0].length;
		
		object_width = width / objects.length;
		object_height = height / objects[0].length;
		
		Log.d("width", "block_width: " + block_width);
		Log.d("height", "block_height: " + block_height);
		
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					Bitmap bitmap;
					Texture texture;
					bitmap = null;
					try
					{
					    bitmap = BitmapFactory.decodeStream( activity.getAssets().open( "droid.png" ) );
					}
					catch( Exception ex )
					{
						Log.d("Sample", "Failed loading player texture.");
			            System.exit(-1);
					}	
					
					Mesh mesh;
					mesh = new Mesh( gl, 4, false, true, false );				
					mesh.texCoord(0f, 1f);
					mesh.vertex( i*block_width, j*block_height, 0 );
					mesh.texCoord(1f, 1f);
			        mesh.vertex( i*block_width + object_width, j*block_height, 0 );
			        mesh.texCoord(1f, 0f);
			        mesh.vertex( i*block_width + object_width, j*block_height + object_height, 0 );
			        mesh.texCoord(0f, 0f);
			        mesh.vertex( i*block_width, j*block_height + object_height, 0 );
			        
			        texture = new Texture(gl, bitmap, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        
			        texture.bind();
			        
			        objects[i][j].setMesh(mesh);
				}
				if(objects[i][j] instanceof Polynomio){
					Mesh mesh;
					mesh = new Mesh( gl, 20, false, false, false );
					
					mesh.vertex( i*block_width, j*block_height, 0 );
					
					mesh.vertex( i*block_width + object_width, j*block_height, block_height );
			        
			        mesh.vertex( i*block_width + object_width, j*block_height + object_height, 0 );
			        
			        mesh.vertex( i*block_width, j*block_height + object_height, 0 );
			        
			        mesh.vertex( i*block_width, j*block_height, 0 );
			        
			        objects[i][j].setMesh(mesh);       
				}
			}
		}
		
	}
	
	public void setPerspective(GameActivity activity, GL10 gl){
		gl.glViewport(0, 0, activity.getViewportWidth(), activity.getViewportHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		GLU.gluOrtho2D( gl, 0, activity.getViewportWidth(), 0, activity.getViewportHeight());
		
		gl.glMatrixMode( GL10.GL_MODELVIEW );
        gl.glLoadIdentity();
		
	}
	
	public void renderObjects(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		block_width = activity.getViewportWidth() / objects.length;
		block_height = activity.getViewportHeight() / objects[0].length;
		
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					if(objects[i][j].isMovingLeft){
						if(objects[i][j].pixel_position.x == -1){
							objects[i][j].pixel_position.x = objects[i][j].block_position.x*block_width;
						}
						if(objects[i][j].pixel_position.x > i*block_width){
							gl.glEnable( GL10.GL_TEXTURE_2D );
							gl.glPushMatrix();
							gl.glTranslatef(objects[i][j].pixel_position.x, j*block_height, 0 );
							objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
							gl.glPopMatrix();
							gl.glDisable( GL10.GL_TEXTURE_2D );
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
							gl.glEnable( GL10.GL_TEXTURE_2D );
							gl.glPushMatrix();
							gl.glTranslatef(objects[i][j].pixel_position.x, j*block_height, 0 );
							objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
							gl.glPopMatrix();
							gl.glDisable( GL10.GL_TEXTURE_2D );
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
							gl.glEnable( GL10.GL_TEXTURE_2D );
							gl.glPushMatrix();
							gl.glTranslatef(i*block_width,objects[i][j].pixel_position.y, 0 );
							objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
							gl.glPopMatrix();
							gl.glDisable( GL10.GL_TEXTURE_2D );
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
							gl.glEnable( GL10.GL_TEXTURE_2D );
							gl.glPushMatrix();
							gl.glTranslatef(i*block_width,objects[i][j].pixel_position.y, 0 );
							objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
							gl.glPopMatrix();
							gl.glDisable( GL10.GL_TEXTURE_2D );
							objects[i][j].pixel_position.y -= block_height * objects[i][j].movingVelocity;
						}
						else{
							objects[i][j].isMovingDown = false;
							objects[i][j].lastState = Simulation.DOWN;
							objects[i][j].pixel_position.y = -1;
						}
					}
					else{
						gl.glEnable( GL10.GL_TEXTURE_2D );
						gl.glPushMatrix();
						gl.glTranslatef(i*block_width, j*block_height, 0 );
						objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
						gl.glPopMatrix();
						gl.glDisable( GL10.GL_TEXTURE_2D );
					}
				}
				if(objects[i][j] instanceof Polynomio){
					gl.glColor4f(objects[i][j].colors[0],objects[i][j].colors[1],objects[i][j].colors[2],objects[i][j].colors[3]);
					gl.glPushMatrix();
					gl.glTranslatef(i*block_width, j*block_height, 0 );
					objects[i][j].getMesh().render(PrimitiveType.TriangleStrip);
					gl.glPopMatrix();
					gl.glColor4f(1, 1, 1, 1);
				}
			}
		}
	}
}
