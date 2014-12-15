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
	float width = 24.9f/1.5f/1.5f;
	float height = 14.7f/1.5f/1.5f;
	int count;
	
	
	public Renderer3D(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		block_width = width / objects.length;
		block_height = height / objects[0].length;
		
		object_width = width / objects.length;
		object_height = height / objects[0].length;
		
		object_depth = -0.5f;
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
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
					mesh.vertex( block.x*block_width, block.y*block_height, object_depth );
					mesh.texCoord(1f, 1f);
			        mesh.vertex( block.x*block_width + object_width, block.y*block_height, object_depth );
			        mesh.texCoord(1f, 0f);
			        mesh.vertex( block.x*block_width + object_width, block.y*block_height + object_height, object_depth );
			        mesh.texCoord(0f, 0f);
			        mesh.vertex( block.x*block_width, block.y*block_height + object_height, object_depth );
			        
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
					Log.d("test", "i :" + i + "j:" + j);
					count ++;
					Log.d("test", "count: " + count);
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
					mesh = new Mesh( gl, 144*4, false, false, true );
					
					Polynomio polynomio = (Polynomio)objects[i][j];
					for(Block block : polynomio.blocks){
						// Untere Seite
						Vector normal = calculateNormal(new Vector (block.x*block_width, block.y*block_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height,0f),
								new Vector(block.x*block_width + object_width, block.y*block_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, 0f); //1
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, 0f); //2
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, object_depth); //3
						
						normal = calculateNormal(new Vector (block.x*block_width, block.y*block_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height, object_depth),
								new Vector(block.x*block_width, block.y*block_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, 0f); //1
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, object_depth); //3
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, object_depth); //4
						
						//Obere Seite
						normal = calculateNormal(new Vector (block.x*block_width, block.y*block_height + object_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, 0f),
								new Vector(block.x*block_width + object_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f); //a
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, 0f); //b
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, object_depth); //c
						
						normal = calculateNormal(new Vector (block.x*block_width, block.y*block_height + object_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, object_depth),
								new Vector(block.x*block_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f); //a
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, object_depth); //c
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, object_depth); //d
						
						//Linke Seite
						normal = calculateNormal(new Vector (block.x*block_width, block.y*block_height, 0f),
								new Vector (block.x*block_width, block.y*block_height, object_depth),
								new Vector(block.x*block_width, block.y*block_height + object_height, 0f));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, 0f); //1
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, object_depth); //4
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f); //a
						
						normal = calculateNormal(new Vector (block.x*block_width, block.y*block_height, object_depth),
								new Vector (block.x*block_width, block.y*block_height + object_height, 0f),
								new Vector(block.x*block_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, object_depth); //4
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f); //a
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, object_depth); //d
						
						//Rechte Seite
						normal = calculateNormal(
								new Vector (block.x*block_width + object_width, block.y*block_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height, object_depth),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, 0f));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, 0f); //2
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, object_depth); //3
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, 0f); //b
						
						normal = calculateNormal(
								new Vector (block.x*block_width + object_width, block.y*block_height, object_depth),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, object_depth); //3
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, 0f); //b
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, object_depth); //c
						
						//Vordere Seite
						normal = calculateNormal(
								new Vector (block.x*block_width, block.y*block_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height, 0f),
								new Vector (block.x*block_width, block.y*block_height + object_height, 0f));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, 0f); //1
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, 0f); //2
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f); //a
						
						normal = calculateNormal(
								new Vector (block.x*block_width + object_width, block.y*block_height, 0f),
								new Vector (block.x*block_width, block.y*block_height + object_height, 0f),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, 0f));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, 0f); //2
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f); //a
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, 0f); //b
						
						//Hintere Seite
						normal = calculateNormal(
								new Vector (block.x*block_width, block.y*block_height, object_depth),
								new Vector (block.x*block_width + object_width, block.y*block_height, object_depth),
								new Vector (block.x*block_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, object_depth); //4
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, object_depth); //3
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, object_depth); //d
						
						normal = calculateNormal(
								new Vector (block.x*block_width + object_width, block.y*block_height, object_depth),
								new Vector (block.x*block_width, block.y*block_height + object_height, object_depth),
								new Vector (block.x*block_width + object_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height, object_depth); //3
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, object_depth); //d
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, object_depth); //c
						
						
						
						/*normal = calculateNormal(new Vector ( block.x*block_width + object_width, block.y*block_height + object_height, 0f ),
								new Vector (block.x*block_width, block.y*block_height + object_height, 0f),
								new Vector(block.x*block_width, block.y*block_height + object_height, object_depth));
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width + object_width, block.y*block_height + object_height, 0f);
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, 0f);
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height + object_height, object_depth);
						mesh.normal(normal.x,normal.y,normal.z);
						mesh.vertex(block.x*block_width, block.y*block_height, object_depth);*/
       
					}
					textureLocker = new Texture(gl, bitmapLocker, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			       
					objects[i][j].setMesh(mesh);
				}
			}
		}
		
	}
	public Vector calculateNormal(Vector x, Vector y, Vector z){
		Vector a = y.sub(x);
		Vector b = z.sub(x);
		
		Vector normal = new Vector(
				(a.y * b.z) - (a.z * b.y),
				(a.z * b.x) - (a.x * b.z),
				(a.x * b.y) - (a.y * b.x)
				);
		return normal;
	}
	
	
	public void setPerspective(GameActivity activity, GL10 gl){    
        gl.glViewport( 0, 0, activity.getViewportWidth(), activity.getViewportHeight() );
        gl.glClearColor( 0.50f, 0.50f, 0.50f, 0.0f );
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT  | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		float aspectRatio = (float)activity.getViewportWidth() / activity.getViewportHeight();
		GLU.gluPerspective( gl, 67, aspectRatio, 1, 100 );
		
		//gl.glTranslatef(-8.5f, -5.0f, -7.3f);	

		GLU.gluLookAt(gl, 5.5f, 3.38f, 4.75f, 5.5f, 3.38f, -5f, 0f, 1f, 0f);
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
					gl.glColor4f(1, 1, 1, 1);
				}
				if(objects[i][j] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[i][j];
					if(polynomio.isMovingRight){
						polynomio.pixel_position.x += (i - polynomio.blocks.get(polynomio.blocks.size()-1).block_position.x) * block_width;
						polynomio.isMovingRight = false;
					}
					else if(polynomio.isMovingLeft){
						polynomio.pixel_position.x += (i - polynomio.blocks.get(0).block_position.x) * block_width;
						polynomio.isMovingLeft = false;
					}
					else if(polynomio.isMovingUp){
						polynomio.pixel_position.y += (j - polynomio.blocks.get(polynomio.blocks.size()-1).block_position.y) * block_height;
						polynomio.isMovingUp = false;
					}
					else if(polynomio.isMovingDown){
						polynomio.pixel_position.y += (j - polynomio.blocks.get(0).block_position.y) * block_height;
						polynomio.isMovingDown = false;
					}
					else{
						if(polynomio.isLocked){
							//gl.glEnable(GL10.GL_BLEND);
							//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
							gl.glPushMatrix();
							gl.glTranslatef(polynomio.pixel_position.x, polynomio.pixel_position.y, 0 );
							polynomio.getMesh().render(PrimitiveType.Triangles);
							gl.glPopMatrix();
							gl.glColor4f(1, 1, 1, 1);
							//gl.glDisable(GL10.GL_BLEND);
						}
						else{
							//gl.glEnable(GL10.GL_BLEND);
							//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
							gl.glColor4f(objects[i][j].colors[0],objects[i][j].colors[1],objects[i][j].colors[2],0.5f);
							gl.glPushMatrix();
							gl.glTranslatef(polynomio.pixel_position.x, polynomio.pixel_position.y, 0 );
							polynomio.getMesh().render(PrimitiveType.Triangles);
							gl.glPopMatrix();
							gl.glColor4f(1, 1, 1, 1);
							//gl.glDisable(GL10.GL_BLEND);
						}
					}
				}
			}
		}
	}
	
	public void renderPlayer(GL10 gl, GameObject player, float x, float y, float z){
		
		gl.glPushMatrix();
		gl.glTranslatef(x+(block_width/2),y+(block_height/2),z+object_depth/2 );
		gl.glScalef(object_width/2.2f,object_height/2.2f,object_depth/2.2f);
		player.getMesh().render(PrimitiveType.TriangleFan);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_NORMALIZE);
	}
	
	public void enableCoordinates(GL10 gl, GameObject[][] objects){
		coordinates_list = new ArrayList<Mesh>();
		
		for(int x = 0; x < objects.length + 1; x++){
			Mesh mesh = new Mesh(gl, 2, true, false, false);
			mesh.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh.vertex( block_width * x, 0, object_depth );
			mesh.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh.vertex( block_width * x, block_height * objects[0].length, object_depth );
			coordinates_list.add(mesh);
			Mesh mesh_d = new Mesh(gl, 2, true, false, false);
			mesh_d.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_d.vertex( block_width * x, 0, object_depth );
			mesh_d.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_d.vertex( block_width * x, 0, 0 );
			coordinates_list.add(mesh_d);
			Mesh mesh_u = new Mesh(gl, 2, true, false, false);
			mesh_u.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_u.vertex( block_width * x, block_height * objects[0].length, object_depth );
			mesh_u.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_u.vertex( block_width * x, block_height * objects[0].length, 0 );
			coordinates_list.add(mesh_u);
			
		}
		for(int y = 0; y < objects[0].length + 1; y++){
			Mesh mesh = new Mesh(gl, 2, true, false, false);
			mesh.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh.vertex( 0, block_height * y, object_depth);
			mesh.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh.vertex( block_width * objects.length, block_height * y, object_depth );
			coordinates_list.add(mesh);
			Mesh mesh_l = new Mesh(gl, 2, true, false, false);
			mesh_l.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_l.vertex( 0, block_height * y, object_depth);
			mesh_l.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_l.vertex( 0, block_height * y, 0 );
			coordinates_list.add(mesh_l);
			Mesh mesh_r = new Mesh(gl, 2, true, false, false);
			mesh_r.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_r.vertex( block_width * objects.length, block_height * y, object_depth);
			mesh_r.color( 0.4f, 0.4f, 0.4f, 1 );
			mesh_r.vertex( block_width * objects.length, block_height * y, 0 );
			coordinates_list.add(mesh_r);
		}
	}
	public void renderLight(GL10 gl, GameObject[][] objects){
	
		gl.glEnable( GL10.GL_LIGHTING );
		float[] lightColor = { 1, 1, 1, 1 };
		float[] ambientLightColor = {0.5f, 0.5f, 0.5f, 1 };
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientLightColor,0 );
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor,0 );
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightColor,0 );
		float[] direction = { -1 / (float)Math.sqrt(2), -1 / (float)Math.sqrt(2), 0, 0 };
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_POSITION, direction,0 );
		gl.glEnable( GL10.GL_LIGHT0 );
		gl.glEnable( GL10.GL_COLOR_MATERIAL );
		gl.glEnable(GL10.GL_NORMALIZE);
		
		/*
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[] { 0.2f,0.2f, 0.2f, 1f }, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[] { 1f, 1f, 1f, 1f }, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, new float[] { 1f, 1f, 1f, 1f }, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[] { 5f, 5f, 20f, 0f }, 0);
	
		//gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[] { .23f, .09f, .03f, 1f }, 0);
		//gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[] { .55f, .21f, .07f, 1.0f }, 0);
		//gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, new float[] { .58f, .22f, .07f, 1.0f }, 0);
		//gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 51.2f);
		gl.glEnable(GL10.GL_NORMALIZE);
		
	
        gl.glEnable(GL10.GL_LIGHT0);
	    
//	    gl.glShadeModel(GL10.GL_SMOOTH);
	    
	    gl.glEnable(GL10.GL_COLOR_MATERIAL);
	    */
	    
	}
}
