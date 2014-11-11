package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Renderer {
	
	float block_width;
	float block_height;
	
	public Renderer(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		block_width = activity.getViewportWidth() / objects.length;
		block_height = activity.getViewportHeight() / objects[0].length;
		
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
			        mesh.vertex( i*block_width + 100, j*block_height, 0 );
			        mesh.texCoord(1f, 0f);
			        mesh.vertex( i*block_width + 100, j*block_height + 100, 0 );
			        mesh.texCoord(0f, 0f);
			        mesh.vertex( i*block_width, j*block_height + 100, 0 );
			        
			        texture = new Texture(gl, bitmap, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			        
			        texture.bind();
			        
			        objects[i][j].setMesh(mesh);
				}
				if(objects[i][j] instanceof Polynomio){
					Mesh mesh;
					mesh = new Mesh( gl, 20, true, false, false );
					mesh.color(1, 0, 0, 1);
					mesh.vertex( i*block_width, j*block_height, 0 );
					mesh.color(1, 0, 0, 1);
					mesh.vertex( i*block_width + 100, j*block_height, block_height );
			        mesh.color(1, 0, 0, 1);
			        mesh.vertex( i*block_width + 100, j*block_height + 100, 0 );
			        mesh.color(1, 0, 0, 1);
			        mesh.vertex( i*block_width, j*block_height + 100, 0 );
			        mesh.color(1, 0, 0, 1);
			        mesh.vertex( i*block_width, j*block_height, 0 );
			        
			        objects[i][j].setMesh(mesh);       
				}
			}
		}
		
	}
	public void renderObjects(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		block_width = activity.getViewportWidth() / objects.length;
		block_height = activity.getViewportHeight() / objects[0].length;
		
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					gl.glPushMatrix();
					gl.glTranslatef(i*block_width, j*block_height, 0 );
					objects[i][j].getMesh().render(PrimitiveType.TriangleFan);
					gl.glPopMatrix();
					
				}
				if(objects[i][j] instanceof Polynomio){
					gl.glDisable( GL10.GL_TEXTURE_2D );
					gl.glPushMatrix();
					gl.glTranslatef(i*block_width, j*block_height, 0 );
					objects[i][j].getMesh().render(PrimitiveType.TriangleStrip);
					gl.glPopMatrix();
					gl.glEnable( GL10.GL_TEXTURE_2D );
				}
			}
		}
	}
}
