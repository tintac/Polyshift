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
				if(objects[touchedX][touchedY] instanceof Player){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "right")){
						moveObject(touchedX, touchedY, "right");
						touchedX++;
					}
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[touchedX][touchedY];
					polynomio.sortBlocks("right");
					for(int i = 0; i < polynomio.blocks.size(); i++){
						if(!(predictCollision(polynomio.blocks.get(i).x, polynomio.blocks.get(i).y, "right"))){
							moveObject(polynomio.blocks.get(i).x, polynomio.blocks.get(i).y, "right");
							Block block = polynomio.blocks.get(i);
							block.x++;
							polynomio.blocks.set(i, block);
						}
	
					}
					/*Block b1 = polynomio.blocks.get(0);
					Block b2 = polynomio.blocks.get(1);
					Block b3 = polynomio.blocks.get(2);
					Block b4 = polynomio.blocks.get(3);
					b1.block_position = new Vector(b1.x,b1.y,0);
					b2.block_position = new Vector(b2.x,b2.y,0);
					b3.block_position = new Vector(b3.x,b3.y,0);
					b4.block_position = new Vector(b4.x,b4.y,0);
					while(!(predictCollision(b1.x, b1.y, "right"))
					&& !(predictCollision(b2.x, b2.y, "right"))
					&& !(predictCollision(b3.x, b3.y, "right"))
					&& !(predictCollision(b4.x, b4.y, "right"))){
						moveObject(b1.x, b1.y, "right");
						moveObject(b2.x, b2.y, "right");
						moveObject(b3.x, b3.y, "right");
						moveObject(b4.x, b4.y, "right");
						b1.x++;
						b2.x++;
						b3.x++;
						b4.x++;
						touchedX++;
						//Log.d("block", "b1: " + b1.x + "," + b1.y);
						//Log.d("block", "b2: " + b2.x + "," + b2.y);
						//Log.d("block", "b3: " + b3.x + "," + b3.y);
						//Log.d("block", "b4: " + b4.x + "," + b4.y);
						
					}*/
					/*for(int k = 0; k < objects.length; k++){
						for(int l = 0; l < objects[0].length; l++){
							//Log.d("test", k + "," + l + ":" + objects[k][l]);
						}
						
					}*/
				}
			}
			else if(x < touchedX && y == touchedY){
				if(objects[touchedX][touchedY] instanceof Player){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "left")){
						moveObject(touchedX, touchedY, "left");
						touchedX--;
					}
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[touchedX][touchedY];
					polynomio.sortBlocks("left");
					for(int i = 0; i < polynomio.blocks.size(); i++){
						moveObject(polynomio.blocks.get(i).x, polynomio.blocks.get(i).y, "left");
						Block block = polynomio.blocks.get(i);
						block.x--;
						polynomio.blocks.set(i, block);
					}
				}
			}
			else if(y > touchedY && x == touchedX){
				if(objects[touchedX][touchedY] instanceof Player){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "up")){
						moveObject(touchedX, touchedY, "up");
						touchedY++;
					}
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[touchedX][touchedY];
					polynomio.sortBlocks("up");
					for(int i = 0; i < polynomio.blocks.size(); i++){
						if(!(predictCollision(polynomio.blocks.get(i).y, polynomio.blocks.get(i).y, "up"))){
							moveObject(polynomio.blocks.get(i).y, polynomio.blocks.get(i).y, "up");
							Block block = polynomio.blocks.get(i);
							block.y++;
							polynomio.blocks.set(i, block);
						}
	
					}
				}
			}
			else if(y < touchedY && x == touchedX){
				if(objects[touchedX][touchedY] instanceof Player){
					objects[touchedX][touchedY].block_position = new Vector(touchedX,touchedY,0);
					while(!predictCollision(touchedX, touchedY, "down")){
						moveObject(touchedX, touchedY, "down");
						touchedY--;
					}
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					Polynomio polynomio = (Polynomio) objects[touchedX][touchedY];
					polynomio.sortBlocks("down");
					for(int i = 0; i < polynomio.blocks.size(); i++){
						if(!(predictCollision(polynomio.blocks.get(i).y, polynomio.blocks.get(i).y, "down"))){
							moveObject(polynomio.blocks.get(i).y, polynomio.blocks.get(i).y, "down");
							Block block = polynomio.blocks.get(i);
							block.y--;
							polynomio.blocks.set(i, block);
						}
	
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
		
		if(objects[x][y] instanceof Player){
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
		if(objects[x][y] instanceof Polynomio){
			if(direction.equals("right")){
				if(x+1 > PLAYGROUND_MAX_X){
					collision = true;
				}	
				else if(objects[x+1][y] != null){
					if(x+2 <= PLAYGROUND_MAX_X){
						if((!(objects[x+1][y] == objects[x][y]) && objects[x+2][y] == null)){
							collision = true;
						}
					
					}
				}
			}
			if(direction.equals("left")){
				if(x-1 < PLAYGROUND_MIN_X){
					collision = true;
				}	
				else if(objects[x-1][y] != null){
					if(x-2 >= PLAYGROUND_MIN_X){
						if((!(objects[x-1][y] == objects[x][y]) && objects[x-2][y] == null)){
							collision = true;
						}
					
					}
				}
			}
			if(direction.equals("up")){
				if(y+1 > PLAYGROUND_MAX_Y){
					collision = true;
				}	
				else if(objects[x][y+1] != null){
					if(y+2 <= PLAYGROUND_MAX_Y){
						if((!(objects[x][y+1] == objects[x][y]) && objects[x][y+2] == null)){
							collision = true;
						}
					
					}
				}
			}
			if(direction.equals("down")){
				if(y-1 < PLAYGROUND_MIN_Y){
					collision = true;
				}	
				else if(objects[x][y-1] != null){
					if(y-2 <= PLAYGROUND_MIN_Y){
						if((!(objects[x][y-1] == objects[x][y]) && objects[x][y-2] == null)){
							collision = true;
						}
					
					}
				}
			}
		}
		return collision;
	}
	
	public void update(GameActivity activity){
		getTouch(activity);
	}
}
