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
	float added_depth;
	float width = 24.9f/1.5f/1.5f;
	float height = 14.7f/1.5f/1.5f;
	ArrayList<Mesh> finish_list;
	int count;
	
	
	public Renderer3D(GameActivity activity, GL10 gl, GameObject[][] objects){
		
		block_width = width / objects.length;
		block_height = height / objects[0].length;
		
		object_width = width / objects.length;
		object_height = height / objects[0].length;
		
		object_depth = -0.5f;
		
		finish_list = new ArrayList<Mesh>();
		
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
					polynomio.setMesh(blockMesh);
					
					float line_depth =  0.04f;
					if(i+1 < objects.length && objects[i+1][j] != objects[i][j]){
						Mesh border_mesh = new Mesh(gl, 2, true, false, false);
						border_mesh.color( 0f, 0f, 0f, 1 );
						border_mesh.vertex( block_width * (i+1), block_height * j, line_depth );
						border_mesh.color( 0f, 0f, 0f, 1 );
						border_mesh.vertex( block_width * (i+1), block_height * (j+1) , line_depth );
						polynomio.setBorder(border_mesh);
					}
					if(i-1 >= 0 && objects[i-1][j] != objects[i][j]){
						Mesh border_mesh = new Mesh(gl, 2, true, false, false);
						border_mesh.color( 0f, 0f, 0f, 1 );
						border_mesh.vertex( block_width * i, block_height * j, line_depth );
						border_mesh.color( 0f, 0f, 0f, 1 );
						border_mesh.vertex( block_width * i, block_height * (j+1) , line_depth );
						polynomio.setBorder(border_mesh);
					}
					if(j+1 < objects[0].length && objects[i][j+1] != objects[i][j]){
						Mesh border_mesh = new Mesh(gl, 2, true, false, false);
						border_mesh.color( 0f, 0f, 0f, 1 );
						border_mesh.vertex( block_width * i, block_height * (j+1), line_depth );
						border_mesh.color( 0f, 0f, 0f, 1 );
						border_mesh.vertex( block_width * (i+1), block_height * (j+1) , line_depth );
						polynomio.setBorder(border_mesh);
					}
					if(j-1 >= 0 && objects[i][j-1] != objects[i][j]){
						Mesh border_mesh = new Mesh(gl, 2, true, false, false);
						border_mesh.color( 0.4f, 0.4f, 0.4f, 1 );
						border_mesh.vertex( block_width * i, block_height * j, line_depth );
						border_mesh.color( 0.4f, 0.4f, 0.4f, 1 );
						border_mesh.vertex( block_width * (i+1), block_height * j , line_depth );
						polynomio.setBorder(border_mesh);
					}				
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
		
		if(finish_list != null){
			for(Mesh mesh : finish_list){
				mesh.render( PrimitiveType.TriangleStrip );
			}
		}
	
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[i].length; j++){
				if(objects[i][j] instanceof Player){
					if(objects[i][j].isPlayerOne){
						gl.glColor4f((51f/255f*1.5f),(77f/255*1.5f),(100f/255f*1.5f),1f);
						if(objects[i][j].isLocked){
							//gl.glEnable(GL10.GL_BLEND);
							gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
							gl.glColor4f((51f/255f/1.5f),(77f/255/1.5f),(92f/255f/1.5f),0.3f);
						}
					}else{
						gl.glColor4f((223f/255f),(73f/255f),(73f/255f),1.0f);
						if(objects[i][j].isLocked){
							//gl.glEnable(GL10.GL_BLEND);
							gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
							gl.glColor4f((223f/255f/2.5f),(73f/255f/2.5f),(73f/255f/2.5f),0.3f);
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
					if(polynomio.isMovingRight){
						polynomio.pixel_position.x += (i - polynomio.blocks.get(polynomio.blocks.size()-1).block_position.x) * block_width;
						polynomio.border_pixel_position.x += (i - polynomio.blocks.get(polynomio.blocks.size()-1).block_position.x) * block_width;
						polynomio.isMovingRight = false;
					}
					else if(polynomio.isMovingLeft){
						polynomio.pixel_position.x += (i - polynomio.blocks.get(0).block_position.x) * block_width;
						polynomio.border_pixel_position.x += (i - polynomio.blocks.get(0).block_position.x) * block_width;
						polynomio.isMovingLeft = false;
					}
					else if(polynomio.isMovingUp){
						polynomio.pixel_position.y += (j - polynomio.blocks.get(polynomio.blocks.size()-1).block_position.y) * block_height;
						polynomio.border_pixel_position.y += (j - polynomio.blocks.get(polynomio.blocks.size()-1).block_position.y) * block_height;
						polynomio.isMovingUp = false;
					}
					else if(polynomio.isMovingDown){
						polynomio.pixel_position.y += (j - polynomio.blocks.get(0).block_position.y) * block_height;
						polynomio.border_pixel_position.y += (j - polynomio.blocks.get(0).block_position.y) * block_height;
						polynomio.isMovingDown = false;
					}
					else{
						polynomio.pixel_position.x = i *block_width;
						polynomio.pixel_position.y = j *block_height;
					}
					if(polynomio.isLocked){
						gl.glColor4f(objects[i][j].colors[0]/2.5f,objects[i][j].colors[1]/2.5f,objects[i][j].colors[2]/2.5f,0.5f);
						blockRenderer(gl,polynomio,polynomio.pixel_position.x, polynomio.pixel_position.y, polynomio.pixel_position.z );
						gl.glColor4f(1, 1, 1, 1);
						for(Mesh mesh : polynomio.border_list){
							gl.glPushMatrix();
							gl.glTranslatef(polynomio.border_pixel_position.x,polynomio.border_pixel_position.y,0);
							mesh.render(PrimitiveType.Lines);
							gl.glPopMatrix();
						}
					}
					else{
						gl.glColor4f(objects[i][j].colors[0],objects[i][j].colors[1],objects[i][j].colors[2],0.5f);
						blockRenderer(gl,polynomio,polynomio.pixel_position.x, polynomio.pixel_position.y, polynomio.pixel_position.z );
						gl.glColor4f(1, 1, 1, 1);
						for(Mesh mesh : polynomio.border_list){
							gl.glPushMatrix();
							gl.glTranslatef(polynomio.border_pixel_position.x,polynomio.border_pixel_position.y,0);
							mesh.render(PrimitiveType.Lines);
							gl.glPopMatrix();
						}
					}
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
			Mesh mesh_l = new Mesh(gl, 5, true, false, true);
			mesh_l.color((223f/255f)*2.8f,(73f/255f)*2.8f,(73f/255f)*2.8f,1.0f);
			mesh_l.vertex( 0, block_height * y, object_depth);
			mesh_l.color((223f/255f)*2.8f,(73f/255f)*2.8f,(73f/255f)*2.8f,1.0f);
			mesh_l.vertex( 0, block_height * y, 0 );
			mesh_l.color((223f/255f)*2.8f,(73f/255f)*2.8f,(73f/255f)*2.8f,1.0f);
			if(y < objects[0].length){
				mesh_l.vertex( 0, block_height * (y+1), 0 );
			}
			else{
				mesh_l.vertex( 0, block_height * y, 0 );
			}
			mesh_l.color((223f/255f)*2.8f,(73f/255f)*2.8f,(73f/255f)*2.8f,1.0f);
			if(y < objects[0].length){
				mesh_l.vertex( 0, block_height * (y+1), object_depth);
			}
			else{
				mesh_l.vertex( 0, block_height * y, object_depth);
			}
			mesh_l.color((223f/255f)*2.8f,(73f/255f)*2.8f,(73f/255f)*2.8f,1.0f);
			mesh_l.vertex( 0, block_height * y, object_depth);
			finish_list.add(mesh_l);
			coordinates_list.add(mesh_l);
			Mesh mesh_r = new Mesh(gl, 5, true, false, true);
			mesh_r.color((51f/255f*3),(77f/255*3),(92f/255f*3),1f);
			mesh_r.vertex( block_width * objects.length, block_height * y, object_depth);
			mesh_r.color((51f/255f*3),(77f/255*3),(92f/255f*3),1f);
			mesh_r.vertex( block_width * objects.length, block_height * y, 0 );
			mesh_r.color((51f/255f*3),(77f/255*3),(92f/255f*3),1f);
			if(y < objects[0].length){
				mesh_r.vertex( block_width * objects.length, block_height * (y+1), 0 );
			}
			else{
				mesh_r.vertex( block_width * objects.length, block_height * y, 0 );
			}
			mesh_r.color((51f/255f*3),(77f/255*3),(92f/255f*3),1f);
			if(y < objects[0].length){
				mesh_r.vertex( block_width * objects.length, block_height * (y+1), object_depth);
			}
			else{
				mesh_r.vertex( block_width * objects.length, block_height * y, object_depth);
			}
			mesh_r.color((51f/255f*3),(77f/255*3),(92f/255f*3),1f);
			mesh_r.vertex( block_width * objects.length, block_height * y, object_depth);
			finish_list.add(mesh_r);
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
