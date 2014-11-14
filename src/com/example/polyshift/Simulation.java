package com.example.polyshift;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.util.Log;

public class Simulation {
	
	final int PLAYGROUND_MAX_X = 16;
	final int PLAYGROUND_MIN_X = 0;
	final int PLAYGROUND_MAX_Y = 8;
	final int PLAYGROUND_MIN_Y = 0;
	
	ArrayList<Polynomio>polynomios = new ArrayList<Polynomio>();
	
	public GameObject[][] objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];
	int touchedX;
	int touchedY;

	
	public Simulation(GameActivity activity){
		populate();
	}
	
	public void populate(){
		
		ArrayList<int[]>directions = new ArrayList<int[]>();
		
		this.objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];

		directions.add(new int[]{1,1,1,0}); //0 Horizontaler Strich positive X-Achse
		directions.add(new int[]{2,2,2,0}); //1 Horizontaler Strich negative X-Achse//
		directions.add(new int[]{3,3,3,0}); //2 Vertikaler Strich positive Y-Achse
		directions.add(new int[]{4,4,4,0}); //3 Vertikaler Strich negative Y-Achse
		directions.add(new int[]{1,1,3,0}); //4 Liegendes L nach Oben zeigend
		directions.add(new int[]{1,1,4,0}); //5 Liegendes L nach unten zeigend
		directions.add(new int[]{3,3,2,0}); //6 Stehendes L nach Oben zeigend
		directions.add(new int[]{4,4,1,0}); //7 Stehendes L nach unten zeigend
		directions.add(new int[]{1,3,1,0}); //8 S-Form liegend
		directions.add(new int[]{3,1,3,0}); //9 S-Form stehend - rechtsknick
		directions.add(new int[]{3,2,3,0}); //10 S-Form stehend - linksknick
		directions.add(new int[]{1,3,4,1}); //11 Dreieck liegend - nach oben zeigend
		directions.add(new int[]{1,4,3,1}); //12 Dreieck liegend - nach unten zeigend
		directions.add(new int[]{3,2,1,3}); //13 Dreieck stehend - nach links zeigend
		directions.add(new int[]{3,1,2,3}); //14 Dreieck stehend - nach rechts zeigend
		directions.add(new int[]{1,3,2,0}); //15 Quader
		
		Player player = new Player();
		addGameObject(player, PLAYGROUND_MIN_X, PLAYGROUND_MAX_Y/2);
		Player player2 = new Player();

		addGameObject(player2, PLAYGROUND_MAX_X,PLAYGROUND_MAX_Y/2);

		polynomios.add(newPolynomio(directions.get(11),7,0));
		polynomios.add(newPolynomio(directions.get(2),7,1));
		polynomios.add(newPolynomio(directions.get(15),9,1));
		polynomios.add(newPolynomio(directions.get(9),8,2));
		polynomios.add(newPolynomio(directions.get(6),10,3));
		polynomios.add(newPolynomio(directions.get(10),8,4));
		polynomios.add(newPolynomio(directions.get(13),8,6));
		polynomios.add(newPolynomio(directions.get(7),9,8));
		
		for(int i = 0;i<polynomios.size();i++){
			Polynomio polynomio = polynomios.get(i);
			for(int j = 0; j< polynomio.blocks.size();j++){
				Block block = polynomio.blocks.get(j);
				addGameObject(polynomio, block.x, block.y);
			}
		}
		
	}
	
	public Polynomio newPolynomio(int[] direction, int startX, int startY){
		return (new Polynomio(direction, 4, startX, startY));

	}
	
	public void addGameObject(GameObject object, int x, int y){
		objects[x][y] = object;
	}
	
	public void getTouch(GameActivity activity){
		if(activity.isTouched()){
			int x = Math.round(activity.getTouchX() / (activity.getViewportWidth() / objects.length));
			int y = Math.round(objects[0].length - (activity.getTouchY() / (activity.getViewportHeight() / objects[0].length)) - 1);
			if(x > touchedX && y == touchedY){
				if(objects[touchedX][touchedY] != null){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "right")){
						moveObject(touchedX, touchedY, "right");
						touchedX++;
					}
				}
				/* if(objects[touchedX][touchedY] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[touchedX][touchedY];
					polynomio.block_position = new Vector(touchedX,touchedY,0);
					while(!(predictCollision(polynomio.getBlocks().get(0).x, polynomio.getBlocks().get(0).y, "right") 
					&& predictCollision(polynomio.getBlocks().get(1).x, polynomio.getBlocks().get(1).y, "right")
					&& predictCollision(polynomio.getBlocks().get(2).x, polynomio.getBlocks().get(2).y, "right")
					&& predictCollision(polynomio.getBlocks().get(3).x, polynomio.getBlocks().get(3).y, "right"))){
						moveObject(polynomio.getBlocks().get(0).x, polynomio.getBlocks().get(0).y, "right");
						moveObject(polynomio.getBlocks().get(1).x, polynomio.getBlocks().get(1).y, "right");
						moveObject(polynomio.getBlocks().get(2).x, polynomio.getBlocks().get(2).y, "right");
						moveObject(polynomio.getBlocks().get(3).x, polynomio.getBlocks().get(3).y, "right");
						touchedX++;
					}
				} */
			}
			else if(x < touchedX && y == touchedY){
				if(objects[touchedX][touchedY] != null){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "left")){
						moveObject(touchedX, touchedY, "left");
						touchedX--;
					}
				}
			}
			else if(y > touchedY && x == touchedX){
				if(objects[touchedX][touchedY] != null){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "up")){
						moveObject(touchedX, touchedY, "up");
						touchedY++;
					}
				}
			}
			else if(y < touchedY && x == touchedX){
				if(objects[touchedX][touchedY] != null){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "down")){
						moveObject(touchedX, touchedY, "down");
						touchedY--;
					}
				}
			}
			touchedX = x;
			touchedY = y;
		}
	}
	public void moveObject(int x, int y, String direction){
		
		if(objects[x][y] != null){
			if(direction.equals("right")){
				objects[x][y].isMovingRight = true;
				objects[x+1][y] = objects[x][y];
				objects[x][y] = null;
			}
			if(direction.equals("left")){
				objects[x][y].isMovingLeft = true;
				objects[x-1][y] = objects[x][y];
				objects[x][y] = null;
			}
			if(direction.equals("up")){
				objects[x][y].isMovingUp = true;
				objects[x][y+1] = objects[x][y];
				objects[x][y] = null;
			}
			if(direction.equals("down")){
				objects[x][y].isMovingDown = true;
				objects[x][y-1] = objects[x][y];
				objects[x][y] = null;
			}
		}
		
	}
	public boolean predictCollision(int x, int y, String direction){
		boolean collision = false;
		
		if(objects[x][y] != null){
			if(direction.equals("right")){
				if((x+1 > PLAYGROUND_MAX_X || objects[x+1][y] != null)){
					collision = true;
				}
			}
			if(direction.equals("left")){
				if((x-1 < PLAYGROUND_MIN_X || objects[x-1][y] != null)){
					collision = true;
				}
			}
			if(direction.equals("up")){
				if((y+1 > PLAYGROUND_MAX_Y || objects[x][y+1] != null)){
					collision = true;
				}
			}
			if(direction.equals("down")){
				if((y-1 < PLAYGROUND_MIN_Y || objects[x][y-1] != null)){
					collision = true;
				}
			}
		}
		Log.d("col", "collision: " + collision);
		return collision;
	}
	
	public void update(GameActivity activity){
		getTouch(activity);
	}
}
