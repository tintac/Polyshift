package com.example.polyshift;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.util.Log;

import com.example.polyshift.Mesh.PrimitiveType;
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
		
		Mesh blockMesh = null;
		try {
			blockMesh = MeshLoader.loadObj(gl, activity.getAssets().open( "block.obj" ) );
		} catch (IOException e1) {
			Log.d("Fehler:","..loading cube");
		}
		
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					try {
						Mesh mesh;
						mesh = MeshLoader.loadObj(gl, activity.getAssets().open( "sphere.obj" ) );
						objects[i][j].setMesh(mesh);	
						
					} catch (IOException e1) {
						Log.d("Fehler:","..loading sphere");
					}	
				}
				if(objects[i][j] instanceof Polynomio){
						Polynomio polynomio = (Polynomio) objects[i][j];
						Float random = (float) (Math.random()* (0 - -0.1) + -0.1);
						polynomio.pixel_position.z = random;
						polynomio.setMesh(blockMesh);
				}
			}
		}
	}
	
	public void setPerspective(GameActivity activity, GL10 gl){    
        gl.glViewport( 0, 0, activity.getViewportWidth(), activity.getViewportHeight() );
        gl.glClearColor( 238f/255f, 233f/255f, 192f/255f, 0.0f );
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT  | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		float aspectRatio = (float)activity.getViewportWidth() / activity.getViewportHeight();
		GLU.gluPerspective( gl, 67, aspectRatio, 1, 100 );
		GLU.gluLookAt(gl, 5.5f, 3.38f, 4.8f, 5.5f, 3.38f, -5f, 0f, 1f, 0f);
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
						gl.glColor4f((51f/255f),(77f/255),(92f/255f),1f);
						if(objects[i][j].isLocked){
							gl.glEnable(GL10.GL_BLEND);
							gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
							gl.glColor4f((51f/255f),(77f/255),(92f/255f),0.3f);
						}
					}else{
						gl.glColor4f((223f/255f),(73f/255f),(73f/255f),1.0f);
						if(objects[i][j].isLocked){
							gl.glEnable(GL10.GL_BLEND);
							gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
							gl.glColor4f((223f/255f),(73f/255f),(73f/255f),0.3f);
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
					
					gl.glDisable(GL10.GL_BLEND);
					gl.glEnable(GL10.GL_COLOR_MATERIAL);
					gl.glColor4f(1, 1, 1, 1);
				}
				if(objects[i][j] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[i][j];
					gl.glColor4f(polynomio.colors[0],polynomio.colors[1],polynomio.colors[2],1f);
					if(polynomio.isMovingRight){
						for(Block block : polynomio.blocks){
							if(polynomio.pixel_position.x == -1){
								polynomio.pixel_position.x = block.x*block_width;
							}
							if(polynomio.pixel_position.x < block.x*block_width){
								blockRenderer(gl, polynomio,polynomio.pixel_position.x, block.y*block_height, 0 );
								polynomio.pixel_position.x += block_width * polynomio.movingVelocity;
							}
							else{
								blockRenderer(gl, polynomio,polynomio.pixel_position.x, block.y*block_height, 0 );
								polynomio.isMovingRight = false;
								polynomio.lastState = Simulation.RIGHT;
								polynomio.pixel_position.x = -1;
							}
						
						}
					}
					else if(polynomio.isMovingLeft){
						
						for(Block block : polynomio.blocks){
							if(polynomio.pixel_position.x == -1 ){
								polynomio.pixel_position.x = block.x*block_width;
							}
							if(polynomio.pixel_position.x > i*block_width){
								blockRenderer(gl, polynomio,polynomio.pixel_position.x, block.y*block_height, 0 );
								polynomio.pixel_position.x -= block_width * polynomio.movingVelocity;
								
							}
							else{
								polynomio.isMovingLeft = false;
								polynomio.lastState = Simulation.LEFT;
								polynomio.pixel_position.x = -1;
							}
						}
					}
					else if(polynomio.isMovingUp){
						for(Block block : polynomio.blocks){
							if(polynomio.pixel_position.y == -1){
								polynomio.pixel_position.y = block.y*block_height;
							}
							if(polynomio.pixel_position.y < j*block_height){
								blockRenderer(gl, polynomio,block.x*block_width,polynomio.pixel_position.y, 0 );
								polynomio.pixel_position.y += block_height * polynomio.movingVelocity;
							}
							else{
								polynomio.isMovingUp = false;
								polynomio.lastState = Simulation.UP;
								polynomio.pixel_position.y = -1;
							}
						}
					}
					else if(polynomio.isMovingDown){
						for(Block block : polynomio.blocks){
							if(polynomio.pixel_position.y == -1){
								polynomio.pixel_position.y = block.y*block_height;
							}
							if(polynomio.pixel_position.y >= j*block_height){
								blockRenderer(gl, polynomio,block.x*block_width,polynomio.pixel_position.y, 0 );
								polynomio.pixel_position.y -= block_height * polynomio.movingVelocity;
							}
							else{
								polynomio.isMovingDown = false;
								polynomio.lastState = Simulation.DOWN;
								polynomio.pixel_position.y = -1;
							}
						}
					}
					else{
						polynomio.pixel_position.x = i *block_width;
						polynomio.pixel_position.y = j *block_height;
					}
					if(polynomio.isLocked){
						gl.glEnable(GL10.GL_BLEND);
						gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
						gl.glColor4f(polynomio.colors[0],polynomio.colors[1],polynomio.colors[2],0.5f);
						blockRenderer(gl,polynomio,polynomio.pixel_position.x, polynomio.pixel_position.y, polynomio.pixel_position.z );
						gl.glColor4f(1, 1, 1, 1);
						gl.glDisable(GL10.GL_BLEND);
					}
					else{
						blockRenderer(gl,polynomio,polynomio.pixel_position.x, polynomio.pixel_position.y, polynomio.pixel_position.z );
					}
					gl.glColor4f(1, 1, 1, 1);
				}
			}
		}
	}
	
	private void blockRenderer(GL10 gl,GameObject polynomio, float x, float y, float z){
		gl.glPushMatrix();
		gl.glTranslatef(x+(block_width/2),y+(block_height/2),z+object_depth/2 );
		gl.glScalef(object_width*0.88f,object_height*3.4f,object_depth);
		polynomio.getMesh().render(PrimitiveType.Triangles);
		gl.glPopMatrix();
	}
	public void renderPlayer(GL10 gl, GameObject player, float x, float y, float z){
		gl.glPushMatrix();
		gl.glTranslatef(x+(block_width/2),y+(block_height/2),z+object_depth/2 );
		gl.glScalef(object_width/2.2f,object_height/2.2f,object_depth/2.2f);
		player.getMesh().render(PrimitiveType.Triangles);
		gl.glPopMatrix();
		
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
	public void renderLight(GL10 gl){
	
		gl.glEnable( GL10.GL_LIGHTING );
		float[] lightColor = { 1, 1, 1, 1 };
		float[] ambientLightColor = {0.3f, 0.3f, 0.3f, 1 };
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientLightColor,0 );
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor,0 );
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightColor,0 );
		float[] direction = { 0f, 0f, -10f, 0 };
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_POSITION, direction,0 );
		gl.glEnable( GL10.GL_LIGHT0 );
		gl.glEnable( GL10.GL_COLOR_MATERIAL );
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_NORMALIZE);
		
		
	    
	}
}
